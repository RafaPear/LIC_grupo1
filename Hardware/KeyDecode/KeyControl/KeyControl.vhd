library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity KeyControl is
    port (
        clk: in std_logic;
        rst: in std_logic;
        Kack: in std_logic;
        Kpress: in std_logic;
        Kscan: out std_logic;
        Kval: out std_logic
    );
end KeyControl;

architecture behavioral of KeyControl is
    signal state, next_state : std_logic;
    
begin
    process (clk, rst)
    begin
        if rst = '1' then
            state <= '0';
        elsif rising_edge(clk) then
            state <= next_state;
        end if;
    end process;
    
    process (state, Kack, Kpress)
    begin
        case state is
            when '0' =>
                Kscan <= '1';
                Kval <= '0';
                if Kpress = '1' then
                    next_state <= '1';
                else
                    next_state <= '0';
                end if;
            
            when '1' =>
                Kscan <= '0';
                Kval <= '1';
                if (Kpress = '0' and Kack = '1') then
                    next_state <= '0';
                else
                    next_state <= '1';
                end if;
        end case;
    end process;
end behavioral;