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
            wait for clk_period*2 / 2;
            clk <= '1';
            wait for clk_period*2 / 2;
        end loop;
    end process;

    stimulus: process
    begin

        init <= '1';
        wait for clk_period*2;
        init <= '0';

        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;

        assert (error_signal = '0') report "Test 1 failed: Expected even parity (0)" severity error;

        wait for clk_period*2;


        init <= '1';
        wait for clk_period*2;
        init <= '0';
       
        data_in <= '1'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;

        assert (error_signal = '1') report "Test 2 failed: Expected odd parity (1)" severity error;

        wait for clk_period*2;


        init <= '1';
        wait for clk_period*2;
        init <= '0';
        
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;

        assert (error_signal = '0') report "Test 3 failed: Expected even parity (0)" severity error;

        wait for clk_period*2;
        

        init <= '1';
        wait for clk_period*2;
        init <= '0';
       
        data_in <= '1'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;

        assert (error_signal = '1') report "Test 4 failed: Expected odd parity (1)" severity error;

        wait for clk_period*2;


        init <= '1';
        wait for clk_period*2;
        init <= '0';
        

        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;

        assert (error_signal = '1') report "Test 5 failed: Expected odd parity (1)" severity error;

        wait for clk_period*2;

        init <= '1';
        wait for clk_period*2;
        init <= '0';

        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;
        data_in <= '1'; wait for clk_period*2;
        data_in <= '0'; wait for clk_period*2;

        assert (error_signal = '0') report "Test 6 failed: Expected even parity (0)" severity error;

        wait for clk_period*2;

        init <= '1';
        wait for clk_period*2;
        init <= '0';

    end process;

end Behavioral;