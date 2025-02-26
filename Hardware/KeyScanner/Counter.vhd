library ieee;
use ieee.std_logic_1164.all;

entity Counter is
    port(
        D: in std_logic_vector(1 downto 0);
        RESET: in std_logic;
        CE: in std_logic;
        CLK: in std_logic;
        
    );
end Counter;

architecture arc_counter of Counter is 
    component Reg2 is
        port(	
    		D : in std_logic_vector(1 downto 0);
    		RESET : in std_logic;
    		EN : in std_logic;
    		CLK : in std_logic;
    		Q : out std_logic_vector(1 downto 0)
    	);
    end component;

    begin

end arc_counter;