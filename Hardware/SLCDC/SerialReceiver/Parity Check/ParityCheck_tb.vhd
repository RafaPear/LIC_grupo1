library ieee;
use ieee.std_logic_1164.all;

entity ParityCheck_tb is
end ParityCheck_tb;

architecture Behavioral of ParityCheck_tb is
    component ParityCheck is
        port(
            clk: in std_logic;
            init: in std_logic;
            data_in: in std_logic;
            error: out std_logic
        );
    end component;

    signal clk: std_logic := '0';
    signal init: std_logic := '0';
    signal data_in: std_logic := '0';
    signal error_signal: std_logic;

    constant clk_period: time := 10 ns;

begin

    UUT: ParityCheck
        port map (
            clk => clk,
            init => init,
            data_in => data_in,
            error => error_signal
        );

    clk_process: process
    begin
        while true loop
            clk <= '0';
            wait for clk_period / 2;
            clk <= '1';
            wait for clk_period / 2;
        end loop;
    end process;

    stim_proc: process
    begin
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        -- Test 1: Trama with even number of 1s (10100)
        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;

        -- Check the result
        assert (error_signal = '0') report "Test case 1 failed: Expected even parity" severity error;

        
        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        -- Test 2: Trama with odd number of 1s (11001)
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '1'; wait for clk_period;

        -- Check the result
        assert (error_signal = '1') report "Test case 2 failed: Expected odd parity" severity error;


        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        -- Test 3: Trama with no 1s (00000)
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;
        data_in <= '0'; wait for clk_period;

        -- Check the result
        assert (error_signal = '0') report "Test case 3 failed: Expected even parity" severity error;


        init <= '1';
        wait for clk_period;
        init <= '0';
        wait for clk_period;

        -- Test 4: Trama with all 1s (11111)
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;
        data_in <= '1'; wait for clk_period;

        -- Check the result
        assert (error_signal = '1') report "Test case 4 failed: Expected odd parity" severity error;

        wait;
    end process;

end Behavioral;