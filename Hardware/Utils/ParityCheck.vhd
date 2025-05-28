library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck is
    port(
        clk     : in std_logic;
        init    : in std_logic;
        data_in : in std_logic;
        error   : out std_logic
    );
end ParityCheck;

architecture Behavioral of ParityCheck is
component FFD is
    port(
	    CLK: in std_logic;
	    RESET: in std_logic;
	    SET: in std_logic;
	    D: in std_logic;
	    EN: in std_logic;
	    Q: out std_logic
    );
end component;

    signal Q: std_logic := '0';
    signal tempD: std_logic;
begin
    UFFD: FFD
        port map(
            CLK => clk,
            RESET => init,
            SET => '0',
            D => tempD,
            EN => '1',
            Q => Q
        );
    tempD <= data_in xor Q; -- XOR the input data with the current state

    error <= not Q;
end Behavioral;