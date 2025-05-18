library ieee;
use ieee.std_logic_1164.all;

entity FFD is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        SET: in std_logic;
        D: in std_logic;
        EN: in std_logic;
        Q: out std_logic
    );
end FFD;

architecture behavior of FFD is
begin
    process(CLK, RESET, SET)
    begin
        if RESET = '1' then
            Q <= '0';
        elsif SET = '1' then
            Q <= '1';
        elsif rising_edge(CLK) then
            if EN = '1' then
                Q <= D;
            end if;
        end if;
    end process;
end behavior;