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
    begin
        I_TB <= "0000";
        wait for MCLK_HALF_PERIOD*3;
        assert GS_TB = '0' report "Erro: GS deveria ser 0 para entrada 0000" severity error;
        
        I_TB <= "0001";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "00" report "Erro: Saída incorreta para entrada 0001" severity error;
        
        I_TB <= "0010";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "01" report "Erro: Saída incorreta para entrada 0010" severity error;
        
        I_TB <= "0100";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "10" report "Erro: Saída incorreta para entrada 0100" severity error;
        
        I_TB <= "1000";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "11" report "Erro: Saída incorreta para entrada 1000" severity error;
        
        I_TB <= "1001";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "11" report "Erro: Saída incorreta para entrada 1001" severity error;
        
        I_TB <= "1010";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "11" report "Erro: Saída incorreta para entrada 1010" severity error;
        
        I_TB <= "1100";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "11" report "Erro: Saída incorreta para entrada 1100" severity error;
        
        I_TB <= "0101";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "10" report "Erro: Saída incorreta para entrada 0101" severity error;
        
        I_TB <= "0110";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "10" report "Erro: Saída incorreta para entrada 0110" severity error;
        
        I_TB <= "0111";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "10" report "Erro: Saída incorreta para entrada 0111" severity error;
        
        I_TB <= "0011";
        wait for MCLK_HALF_PERIOD*3;
        assert Y_TB = "01" report "Erro: Saída incorreta para entrada 0011" severity error;

        report "Testbench concluído sem erros" severity note;
        wait;
    end process;
end test;

