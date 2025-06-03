library ieee;
use ieee.std_logic_1164.all;

entity Counter is
    port(
        RESET: in std_logic;
        CE: in std_logic;
        CLK: in std_logic;
        Q: out std_logic_vector(3 downto 0)
    );
end Counter;

architecture arc_counter of Counter is
component ADDER4 is
    port (
		A: in std_logic_vector (3 downto 0);
		B: in std_logic_vector (3 downto 0);
		CIN: in std_logic;
		S: out std_logic_vector (3 downto 0);
		COUT: out std_logic
	);
end component;

component REG4 is
    port(
		D: in std_logic_vector(3 downto 0);
		RESET: in std_logic;
        SET: in std_logic;
		EN: in std_logic;
		CLK: in std_logic;
		Q: out std_logic_vector(3 downto 0)
	);
end component;

signal temp_S: std_logic_vector(3 downto 0);
signal temp_Q: std_logic_vector(3 downto 0);

begin

    ADDER4_inst: ADDER4 port map(
        A => "0000",
        B => temp_Q,
        CIN => '1',
        S => temp_S,
        COUT => open
    );

    REG4_inst: REG4 port map(
        D => temp_S,
        RESET => RESET,
        SET => '0',
        EN => CE,
        CLK => CLK,
        Q => temp_Q
    );

    Q <= temp_Q;
end arc_counter;