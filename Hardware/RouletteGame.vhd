library ieee;
use ieee.std_logic_1164.all;

entity RouletteGame is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        Kack: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        COL: out std_logic_vector(3 downto 0);
        LCD_DATA: out std_logic_vector(7 downto 4);
        LCD_RS: out std_logic;
        LCD_EN: out std_logic
    );
end RouletteGame;

architecture arch_RouletteGame of RouletteGame is
    component UsbPort is
        port(
            inputPort : in std_logic_vector(7 downto 0);
            outputPort : out std_logic_vector(7 downto 0)
        );
    end component;

    component KeyDecode is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            Kack: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            Kval: out std_logic;
            COL: out std_logic_vector(3 downto 0);
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    component SLCDC is
        port(
            clk: in std_logic;
            RESET: in std_logic;
            LCDsel: in std_logic;
            SCLK: in std_logic;
            SDX: in std_logic;
            E: out std_logic;
            Dout: out std_logic_vector(4 downto 0)
        );
    end component;

    signal temp_Kval: std_logic;
    signal temp_K: std_logic_vector(3 downto 0);
    signal temp_inPort: std_logic_vector(7 downto 0);
    signal temp_outPort: std_logic_vector(7 downto 0);

begin

    KeyDecode_inst: KeyDecode port map(
        CLK => CLK,
        RESET => RESET,
        Kack => Kack,
        LIN => LIN,
        Kval => temp_Kval,
        COL => COL,
        K => temp_K
    );

    UsbPort_inst: UsbPort port map(
        inputPort => temp_inPort,
        outputPort => temp_outPort
    );

    temp_inPort(3 downto 0) <= temp_K;
    temp_inPort(4) <= temp_Kval;

    LCD_DATA <= temp_outPort(3 downto 0);
    LCD_RS <= temp_outPort(4);
    LCD_EN <= temp_outPort(5);
end arch_RouletteGame;