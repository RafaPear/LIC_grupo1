library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity MAC is
    Port (
        clk: in std_logic;
        reset: in std_logic;
        incPut: in std_logic;
        incGet: in std_logic;
        putget: in std_logic;
        putAddr: out std_logic_vector(3 downto 0);
        getAddr: out std_logic_vector(3 downto 0);
        full: out std_logic;
        empty: out std_logic
    );
end MAC;

architecture arc_MAC of MAC is
begin

    full <= '1' when ((putIndex + 1) mod 16 = getIndex) else '0';
    empty <= '1' when (putIndex = getIndex) else '0';

end arc_MAC;