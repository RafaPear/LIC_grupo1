library ieee;
use ieee.std_logic_1164.all;

entity Decoder_tb is
end Decoder_tb;

architecture behavioral of Decoder_tb is
    
    component Decoder is
    port (
        S : in std_logic_vector(1 downto 0);
        D : out std_logic_vector(3 downto 0)
    );
    end component;

-- UUT signals
constant MCLK_PERIOD : time := 20ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal temp_S: in std_logic_vector(1 downto 0);
signal temp_D: out std_logic_vector(3 downto 0);

begin 
-- Unit Under Test~
UUT: Decoder port map (
    S => temp_S, 
    D => temp_D
    );

stimulus : process
begin 
    temp_S <= "00";
    wait for MCLK_HALF_PERIOD*3;
    assert temp_D = "0001" report "Erro: Saída incorreta para a entrada 00" severity error;
    
    temp_S <= "01";
    wait for MCLK_HALF_PERIOD*3;
    assert temp_D = "0010" report "Erro: Saída incorreta para a entrada 01" severity error;
    
    temp_S <= "10";
    wait for MCLK_HALF_PERIOD*3;
    assert temp_D = "0100" report "Erro: Saída incorreta para a entrada 10" severity error;
    
    temp_S <= "11";
    wait for MCLK_HALF_PERIOD*3;
    assert temp_D = "1000" report "Erro: Saída incorreta para a entrada 11" severity error;
    
    report "Testbench concluída sem erros" severity note;
    wait;
end process;
end behavioral;