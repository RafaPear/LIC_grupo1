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
    signal Q: std_logic := '0';
begin
    process(clk)
    begin
        if rising_edge(clk) then
            if init = '1' then
                Q <= '0';
            else
                Q <= Q xor data_in;
            end if;
        end if;
    end process;

    error <= Q;
end Behavioral;