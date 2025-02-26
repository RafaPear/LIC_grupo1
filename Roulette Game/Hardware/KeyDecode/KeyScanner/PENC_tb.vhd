library ieee;
use ieee.std_logic_1164.all;

entity PENC_tb is
end PENC_tb;

architecture test of PENC_tb is
    component PENC
        port (
            I: in std_logic_vector (3 downto 0);
            Y: out std_logic_vector (1 downto 0);
            GS: out std_logic
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal I_TB: std_logic_vector (3 downto 0);
    signal Y_TB: std_logic_vector (1 downto 0);
    signal GS_TB: std_logic;

    begin 
    
    UUT: PENC port map (
        I => I_TB,
        Y => Y_TB, 
        GS => GS_TB
    );

    stimulus: process
    I <= "0000";
    begin
        wait for MCLK_HALF_PERIOD;
        I <= "0001";
        wait for MCLK_HALF_PERIOD;
        I <= "0010";
        wait for MCLK_HALF_PERIOD;
        I <= "0100";
        wait for MCLK_HALF_PERIOD;
        I <= "1000";
        wait for MCLK_HALF_PERIOD;
        I <= "1001";
        wait for MCLK_HALF_PERIOD;
        I <= "1010";
        wait for MCLK_HALF_PERIOD;
        I <= "1100";
        wait for MCLK_HALF_PERIOD;
        I <= "0101";
        wait for MCLK_HALF_PERIOD;
        I <= "0110";
        wait for MCLK_HALF_PERIOD;
        I <= "0111";
        wait for MCLK_HALF_PERIOD;
        I <= "0011";
    end process;
