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

architecture Behavioral of ParityCheck is
    signal Q: std_logic := '0';
    signal xor_out: std_logic;
    
begin
    xor_out <= data_in xor Q;

    process (clk, init)
    begin
        if init = '1' then
            Q <= '0';
        elsif rising_edge(clk) then
            Q <= xor_out;
        end if;
    end process;

    error <= not Q;
end Behavioral;