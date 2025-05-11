library ieee;
use ieee.std_logic_1164.all;

entity ADDER2 is
	port (
		A: in std_logic_vector (3 downto 0);
		CIN: in std_logic;
		S: out std_logic_vector (3 downto 0)
	);
	end ADDER2;
	
architecture arch_adder of ADDER2 is
component fulladder
	port ( 
		A: in std_logic;
		B: in std_logic;
		Cin: in std_logic;
		R: out std_logic;
		Cout: out std_logic
	);
end component;

signal C1,C2,C3: std_logic;

begin 
	FA0: fulladder port map (
		A => A(0), 
		B => '0', 
		Cin => CIN, 
		R => S(0), 
		Cout => C1
	);

	FA1: fulladder port map (
		A => A(1), 
		B => '0', 
		Cin => C1, 
		R => S(1), 
		Cout => C2
	);
	
	FA2: fulladder port map (
		A => A(2), 
		B => '0', 
		Cin => C2, 
		R => S(2), 
		Cout => C3
	);

	FA3: fulladder port map (
		A => A(3), 
		B => '0', 
		Cin => C3, 
		R => S(3)
	);
end arch_adder;		