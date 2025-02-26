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
    assert (GS_TB = '1') report "Error: GS_TB = 1" severity error;
    begin
        wait for MCLK_HALF_PERIOD*3;
        I <= "0001";
        assert (Y_TB != "00") report "Error: Y_TB != 00" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0010";
        assert (Y_TB != "01") report "Error: Y_TB != 01" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0100";
        assert (Y_TB != "10") report "Error: Y_TB != 10" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "1000";
        assert (Y_TB != "11") report "Error: Y_TB != 11" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "1001";
        assert (Y_TB != "11") report "Error: Y_TB != 11" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "1010";
        assert (Y_TB != "11") report "Error: Y_TB != 11" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "1100";
        assert (Y_TB != "11") report "Error: Y_TB != 11" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0101";
        assert (Y_TB != "10") report "Error: Y_TB != 10" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0110";
        assert (Y_TB != "10") report "Error: Y_TB != 10" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0111";
        assert (Y_TB != "10") report "Error: Y_TB != 10" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        wait for MCLK_HALF_PERIOD*3;
        I <= "0011";
        assert (Y_TB != "01") report "Error: Y_TB != 01" severity error;
        assert (GS_TB = '0') report "Error: GS_TB = 0" severity error;
        I_TB <= "0000";
        wait;
        report "Testbench concluído sem erros" severity note;
    end process;
end test;

