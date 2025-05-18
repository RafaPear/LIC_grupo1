library ieee;
use ieee.std_logic_1164.all;

entity fulladder is
    port ( 
        A: in std_logic;
        B: in std_logic;
        Cin: in std_logic;
        R: out std_logic;
        Cout: out std_logic
    );
end fulladder;

architecture behavior of fulladder is
begin
    R <= A xor B xor Cin;
    Cout <= (A and B) or (Cin and (A xor B));
end behavior;