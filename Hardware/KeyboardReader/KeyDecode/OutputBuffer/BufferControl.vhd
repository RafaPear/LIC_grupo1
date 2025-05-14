library ieee;
use ieee.std_logic_1164.all;

entity BufferControl is
    port (
        clk: in std_logic; 
        reset: in std_logic;   
        Load: in std_logic;  
        ACK: in std_logic;  
        Wreg: in std_logic;  
        OBfree: out std_logic; 
        Dval: out std_logic  
    );
end BufferControl;

architecture Behavioral of BufferControl is
    type stateType is (Start, LoadData, Valid, WaitACK);
    signal state, next_state: stateType;

begin 

    process(clk, reset)
    begin
        if reset = '1' then
            state <= Start;
        elsif rising_edge(clk) then
            state <= next_state;
        end if;
    end process;

    process(state, Load, ACK, Wreg)
    begin
        case state is
            when Start =>
                if Wreg = '1' then
                    next_state <= LoadData;
                else
                    next_state <= Start;
                end if;

            when LoadData =>
                if Load = '1' then
                    next_state <= Valid;
                else
                    next_state <= LoadData;
                end if;

            when Valid =>
                if ACK = '1' then
                    next_state <= WaitACK;
                else
                    next_state <= Valid;
                end if;

            when WaitACK =>
                if ACK = '0' then
                    next_state <= Start;
                else
                    next_state <= WaitACK;
                end if;
                
            when others =>
                next_state <= Start;
        end case;
    end process;

    process(state)
    begin
        case state is
            when Start =>
                OBfree <= '1';
                Dval <= '0';
                
            when LoadData =>
                OBfree <= '0';
                Dval <= '0';
                
            when Valid =>
                OBfree <= '0';
                Dval <= '1';
                
            when WaitACK =>
                OBfree <= '1';
                Dval <= '0';
                
            when others =>
                OBfree <= '1';
                Dval <= '0';
        end case;
    end process;

end Behavioral;