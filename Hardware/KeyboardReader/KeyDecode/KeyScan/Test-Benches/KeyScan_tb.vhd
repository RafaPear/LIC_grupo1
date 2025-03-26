library ieee;
use ieee.std_logic_1164.all;

entity KScan_tb is
end KScan_tb;

architecture behavioral of KScan_tb is
    component KeyScan
        port (
            CLK: in std_logic;
            RESET: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            K: out std_logic_vector(3 downto 0)
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RESET_tb: std_logic;
    signal LIN_tb: std_logic_vector(3 downto 0);
    signal COL_tb: std_logic_vector(3 downto 0);
    signal K_tb: std_logic_vector(3 downto 0);
begin
    test: KeyScan port map(
        CLK => CLK_tb,
        RESET => RESET_tb,
        LIN => LIN_tb,
        COL => COL_tb,
        K => K_tb
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
        RESET_tb <= '1';
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        
        LIN_tb <= "1110";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1111";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1101";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1111";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1011";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1111";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "0111";
        
        wait for MCLK_HALF_PERIOD / 2;

        LIN_tb <= "1111";
        
        wait for MCLK_HALF_PERIOD / 2;
    end process;
end behavioral;