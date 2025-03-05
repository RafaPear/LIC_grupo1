library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;
use ieee.std_logic_unsigned.all;

entity KeyControl is
    port (
        clk: in std_logic;
        reset: in std_logic;
        kack: in std_logic;
        kpress: in std_logic;
        kscan_pulso: out std_logic;
        kval_out: out std_logic
    );
end KeyControl;

architecture behavioral of KeyControl is
    signal state, next_state : std_logic_vector(1 downto 0);
    
begin
    process (clk, reset)
    begin
        if reset = '1' then
            state <= "00";
        elsif rising_edge(clk) then
            state <= next_state;
        end if;
    end process;
    
    process (state, kack, kpress)
    begin
        case state is
            when "00" =>
                kscan_pulso <= '0';
                kval_out <= '0';
                if (kack = '1' and kpress = '0') then
                    next_state <= "01";
                else
                    next_state <= "00";
                end if;
            
            when "01" =>
                kscan_pulso <= '1';
                kval_out <= '0';
                if kpress = '1' then
                    next_state <= "11";
                else
                    next_state <= "00";
                end if;
            
            when "11" =>
                kscan_pulso <= '1';
                kval_out <= '1';
                next_state <= "00";
                
            when others =>
                next_state <= "00";
        end case;
    end process;
    
end behavioral;