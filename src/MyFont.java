public class MyFont {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_ITALIC = "\u001B[3m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_BACKGROUNDWHITE = "\u001B[7m";
    public static final String ANSI_DELETE = "\u001B[9m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREY = "\u001B[37m";
    public static final String ANSI_BACKGROUNDBLACK = "\u001B[40m";
    public static final String ANSI_BACKGROUNDRED = "\u001B[41m";
    public static final String ANSI_BACKGROUNDGREEN = "\u001B[42m";
    public static final String ANSI_BACKGROUNDYELLOW = "\u001B[43m";
    public static final String ANSI_BACKGROUNDBLUE = "\u001B[44m";
    public static final String ANSI_BACKGROUNDPURPLE = "\u001B[45m";
    public static final String ANSI_BACKGROUNDCYAN = "\u001B[46m";
    public static final String ANSI_BACKGROUNDGREY = "\u001B[47m";


    public static void showDEAD(){
        System.out.println(MyFont.ANSI_BACKGROUNDRED + "                                        "
                + MyFont.ANSI_RESET+"\n"+MyFont.ANSI_RED+
                "_|_|_|    _|_|_|_|    _|_|    _|_|_|    \n" +
                "_|    _|  _|        _|    _|  _|    _|  \n" +
                "_|    _|  _|_|_|    _|_|_|_|  _|    _|  \n" +
                "_|    _|  _|        _|    _|  _|    _|  \n" +
                "_|_|_|    _|_|_|_|  _|    _|  _|_|_|    \n" + MyFont.ANSI_RESET +
                MyFont.ANSI_BACKGROUNDRED + "                                        " + MyFont.ANSI_RESET+"\n");
    }

}
