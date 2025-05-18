library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity RouletteDispatcher is
    port (
        clk: in std_logic;
        reset: in std_logic;
        D_val: in std_logic;
        data_in: in std_logic_vector(7 downto 0);
        done: out std_logic;

        digit_select: out std_logic_vector(2 downto 0);
        digit_value: out std_logic_vector(4 downto 0);
        update_disp: out std_logic;
        display_on: out std_logic
    );
end RouletteDispatcher;

architecture behavioral of RouletteDispatcher is
    type state_type is (Idle, Decode, Execute, Finish);
    signal state: state_type;
    
    signal cmd_type: std_logic_vector(2 downto 0);
    signal cmd_data: std_logic_vector(4 downto 0);
    
begin
    process(clk, reset)
    begin
        if reset = '1' then
            state <= Idle;
            done <= '0';
            digit_select <= (others => '0'); -- "000"
            digit_value <= (others => '0'); -- "00000"
            update_disp <= '0';
            display_on <= '1';
            cmd_type <= (others => '0'); -- "000"
            cmd_data <= (others => '0'); -- "00000"
        elsif rising_edge(clk) then
            update_disp <= '0';
            done <= '0';
            case state is
                when Idle =>
                    done <= '0';
                    if D_val = '1' then
                        cmd_type <= data_in(7 downto 5);
                        cmd_data <= data_in(4 downto 0);
                        state <= Decode;
                    end if;

                when Decode =>
                    state <= Execute;
                    
                when Execute =>
                    case cmd_type is
                        when "000"|"001"|"010"|"011"|"100"|"101" =>
                            digit_select <= cmd_type(2 downto 0);
                            digit_value <= cmd_data;
                            
                        when "110" =>
                            update_disp <= '1';
                            
                        when "111" =>
                            display_on <= cmd_data(0);

                        when others =>
                            null;
                    end case;
                    state <= Finish;

                when Finish =>
                    done <= '1';
                    state <= Idle;
            end case;
        end if;
    end process;
end behavioral;