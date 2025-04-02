library ieee;
use ieee.std_logic_1164.all;

entity Counter_1s is
    port (
        data_in: in std_logic_vector(4 downto 0);
        clk: in std_logic;
        result: out std_logic                    
    );
end Counter_1s;

architecture arc_counter_1s of Counter_1s is

    signal XOR1, XOR2, XOR3, XOR4: std_logic;

begin
    XOR1 <= data_in(0) xor data_in(1);
    XOR2 <= XOR1 xor data_in(2);
    XOR3 <= XOR2 xor data_in(3);
    XOR4 <= XOR3 xor data_in(4);

    result <= XOR4;

end arc_counter_1s;