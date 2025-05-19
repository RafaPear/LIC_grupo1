library ieee;
use ieee.std_logic_1164.all;

entity KeyDecode is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
		Kack: in std_logic;
        LIN: in std_logic_vector(3 downto 0);
        Kval: out std_logic;
        COL: out std_logic_vector(3 downto 0);
        K: out std_logic_vector(3 downto 0)
    );
end KeyDecode;

architecture arch_KeyDecode of KeyDecode is
    component KeyScan is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            Kscan: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            Kpress: out std_logic;
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    component KeyControl is
        port (
            clk: in std_logic;
            rst: in std_logic;
            Kack: in std_logic;
            Kpress: in std_logic;
            Kscan: out std_logic;
            Kval: out std_logic
        );
    end component;

    component clkDIV is
        generic(div: natural := 500000);
        port (
            clk_in: in std_logic;
            clk_out: out std_logic
        );
    end component;

    signal temp_Kpress, temp_Kscan, CLK_div: std_logic;

begin

    KeyScan_inst: KeyScan port map(
        CLK => CLK,
        RESET => RESET,
        Kscan => temp_Kscan,
        LIN => LIN,
        COL => COL,
        Kpress => temp_Kpress,
        K => K
    );

    KeyControl_inst: KeyControl port map(
        clk => CLK,
        rst => RESET,
        Kack => Kack,
        Kpress => temp_Kpress,
        Kscan => temp_Kscan,
        Kval => Kval
    );


end arch_KeyDecode;