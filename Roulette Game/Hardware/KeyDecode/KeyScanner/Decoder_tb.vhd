library ieee;
use ieee.std_logic_1164.all;

entity Decoder_tb is
end Decoder_tb;

architecture behavioral of Decoder_tb is
    
    component Decoder is
    port (
        A : in std_logic_vector(1 downto 0);
        D : out std_logic_vector(3 downto 0)
    );
    end component;

-- UUT signals
constant MCLK_PERIOD : time := 20ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal temp_A: in std_logic_vector(1 downto 0);
signal temp_D: out std_logic_vector(3 downto 0);

begin 

-- Unit Under Test~
UUT: Decoder port map (
    A => temp_A, 
    D => temp_D
    );

clk_gen : process
begin
    clk_tb <= '0';
    wait for MCLK_HALF_PERIOD;
    clk_tb <= '1';
    wait for MCLK_HALF_PERIOD;
end process;

    