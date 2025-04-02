library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck is
    port(
        clk: in std_logic;
        init: in std_logic;
        data_in: in std_logic;
        error: out std_logic
    );
end ParityCheck;

architecture arc_ParityCheck of ParityCheck is

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

    signal xor_out: std_logic;
    signal Q: std_logic;

begin

    xor_out <= data_in xor Q;

    FFD_inst: FFD
        port map (
            CLK => clk,
            RESET => init,
            SET => '0',
            D => xor_out,
            EN => '1',
            Q => Q
        );

    error <= Q;

end arc_ParityCheck;
