package lowbrain.library;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class fn {
    /**
     * evaluate string
     * @param str string
     * @return return value from evaluation
     */
    public static float eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            float parse() {
                nextChar();
                float x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            float parseExpression() {
                float x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            float parseTerm() {
                float x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            float parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                float x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Float.parseFloat(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = (float)Math.sqrt(x);
                    else if (func.equals("sin")) x = (float)Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = (float)Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = (float)Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = (float)Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    /***
     * check if string is null or empty
     * @param s string
     * @return is null or empty ?
     */
    @Contract("null -> true")
    public static boolean StringIsNullOrEmpty(String s){
        return s == null || s.trim().length() == 0;
    }

    /**
     * generate a random float [min,max]
     * @param max max value
     * @param min min value
     * @return random float
     */
    public static double randomFloat(double min, double max){
        double range = (max - min);
        return (Math.random() * range) + min;
    }

    /**
     * generate a random double [min,max]
     * @param max max value
     * @param min min value
     * @return random double
     */
    public static double randomDouble(double min, double max){
        double range = (max - min);
        return (Math.random() * range) + min;
    }

    /**
     * generate a int float [min,max]
     * @param max max value
     * @param min min value
     * @return random integer
     */
    public static int randomInt(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    /**
     * parse string to int
     * @param s string
     * @param d default value
     * @return parsed string
     */
    public static int toInteger(String s, Integer d){
        try {
            return Integer.parseInt(s);
        }catch (Exception e){
            return d;
        }
    }

    public static boolean isInt(String s) {
        boolean is = false;
        try {
            Integer.parseInt(s);
            is = true;
        } catch (Exception e) {
            is = false;
        }
        return is;
    }

    /**
     * parse string to double with default value
     * @param s string
     * @param d default value
     * @return parsed string
     */
    public static double toDouble(String s, Double d){
        try {
            return Double.parseDouble(s);
        }catch (Exception e){
            return d;
        }
    }

    /**
     * parse string to float with default value
     * @param s string
     * @param d default value
     * @return parsed string
     */
    public static float toFloat(String s, Float d){
        try {
            return Float.parseFloat(s);
        }catch (Exception e){
            return d;
        }
    }

    public static double toDouble(String s, double d) {
        try {
            return Double.parseDouble(s);
        }catch (Exception e){
            return d;
        }
    }

    /***
     * parse string to date (calendar)
     * @param s date as string
     * @param c default calendar date in case of failure
     * @return Calendar
     */
    public static Calendar toDate(String s, Calendar c){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date date = sdf.parse(s);// all done
            Calendar cal = sdf.getCalendar();
            cal.setTime(date);
            return cal;
        }catch (Exception e){
            return c;
        }
    }

    public static double randomBetween(double min, double max, double value, double range, boolean outBound) {
        range = +range;

        if (range == 0)
            return value;

        if (min > value)
            min = value;
        if (max < value)
            max = value;

        double random = fn.randomDouble(value - range, value + range);

        if (outBound)
            return random;

        if (random > max)
            return max;

        if (random < min)
            return min;

        return random;
    }

    public static double randomBetween(double min, double max, double value, float range) {
        return fn.randomBetween(min, max, value, range, false);
    }

    public static double randomBetween(double value, double range) {
        return randomBetween(value, value, value, range, true);
    }

    @Contract(pure = true)
    public static boolean isBetween(int value, int left, int right) {
        return fn.isBetween(value, left, right, false);
    }

    @Contract(pure = true)
    public static boolean isBetween(int value, int left, int right, boolean included) {
        if (included)
            return value >= left && value <= right;

        return value > left && value < right;
    }

    /**
     * return the distance between two players
     * @param p1 player one
     * @param p2 player two
     * @return distance between the two players
     */
    public static double distanceBetweenPlayers(Player p1, Player p2){
        double dist = 0;

        if(!p1.getWorld().equals(p2.getWorld())){
            return -1;
        }

        double x = p1.getLocation().getX() - p2.getLocation().getX();
        double y = p1.getLocation().getY() - p2.getLocation().getY();
        double z = p1.getLocation().getZ() - p2.getLocation().getZ();

        dist = Math.pow(x*x + y*y + z*z, 0.5);

        return dist;
    }

    /***
     * rotate vector on the xy plan
     * @param dir direction's vector
     * @param angleD angle
     * @return rotated vector
     */
    public static Vector rotateYAxis(Vector dir, double angleD) {
        double angleR = Math.toRadians(angleD);
        double x = dir.getX();
        double z = dir.getZ();
        double cos = Math.cos(angleR);
        double sin = Math.sin(angleR);
        return (new Vector(x*cos+z*(-sin), dir.getY(), x*sin+z*cos)).normalize();
    }

    @NotNull
    public static String getServerVersion() {
        String pck = getServerPackage();
        return pck.substring(pck.lastIndexOf('.') + 1);
    }

    public static String getServerPackage() {
        return Bukkit.getServer().getClass().getPackage().getName();
    }

    public static Class getMinecraftClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server."+ getServerVersion() + "." + name);
    }

    public static Class getBukkitClass(String name) throws ClassNotFoundException {
        return Class.forName(getServerPackage() + "." + name);
    }

    @Contract(value = "null, _ -> false; !null, null -> false", pure = true)
    public static boolean same(ItemStack a, ItemStack b) {
        if (a == null || b == null)
            return false;

        if (a.getType() != b.getType())
            return false;

        if (a.hasItemMeta() != b.hasItemMeta())
            return false;

        if (a.hasItemMeta()
                && !fn.StringIsNullOrEmpty(a.getItemMeta().getDisplayName())
                && b.hasItemMeta()
                && !fn.StringIsNullOrEmpty(b.getItemMeta().getDisplayName())) {

            return a.getItemMeta().getDisplayName() == b.getItemMeta().getDisplayName();
        }

        if (a.getMaxStackSize() != b.getMaxStackSize())
            return false;

        if (a.getType().getMaxDurability() != b.getType().getMaxDurability())
            return false;

        if (a.getType().getData().getClass() != b.getType().getData().getClass())
            return false;

        return true;
    }

    public final static NumberFormat MONEY_FORMAT = new DecimalFormat("#0.00");
    public static String toMoney(double value) {
        return MONEY_FORMAT.format(value);
    }
}
