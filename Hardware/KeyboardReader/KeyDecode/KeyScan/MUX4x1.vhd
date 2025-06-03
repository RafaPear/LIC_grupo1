library ieee;
use ieee.std_logic_1164.all;

entity MUX4x1 is
	port(
	A, B, C, D: in std_logic;
	S: in std_logic_vector(1 downto 0);
	O: out std_logic
	);
end MUX4x1;

architecture arq_MUX4x1 of MUX4x1 is

	signal temp_A_O, temp_B_O, temp_C_O, temp_D_O: std_logic;
begin
	temp_A_O <= (not S(1) and not S(0) and A);
	temp_B_O <= (not S(1) and S(0) and B);
	temp_C_O <= (S(1) and not S(0) and C);
	temp_D_O <= (S(1) and S(0) and D);
	O <= temp_A_O or temp_B_O or temp_C_O or temp_D_O;
end arq_MUX4x1;
