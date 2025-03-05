library ieee;
use ieee.std_logic_1164.ALL;

entity KeyControl_tb is
end KeyControl_tb;

architecture behavioral of KeyControl_tb is
    component KeyControl
        port (
            clk: in std_logic;
            rst: in std_logic;
            Kack: in std_logic;
            Kpress: in std_logic;
            Kscan: out std_logic;
            Kval: out std_logic
        );
    end component;

    constant MCLK_PERIOD : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RST_tb: std_logic;
    signal KACK_tb: std_logic;
    signal KPRESS_tb: std_logic;
    signal KSCAN_tb: std_logic;
    signal KVAL_tb: std_logic;
begin
    UUT: KeyControl port map(
        clk => CLK_tb,
        rst => RST_tb,
        Kack => KACK_tb,
        Kpress => KPRESS_tb,
        Kscan => KSCAN_tb,
        Kval => KVAL_tb
    );

    CLK_gen: process
    begin
        CLK_tb <= '0';
        wait for MCLK_HALF_PERIOD;
        CLK_tb <= '1';
        wait for MCLK_HALF_PERIOD;
    end process;

    stimulus: process
    begin
        RST_tb <= '1';
        wait for MCLK_PERIOD;
        RST_tb <= '0';

        wait;
    end process;
end behavioral;
