library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity RingBuffer_tb is
end RingBuffer_tb;

architecture test of RingBuffer_tb is
    component RingBuffer is
        port(
        clk: in std_logic;
        reset: in std_logic;
        CTS: in std_logic;
        DAV: in std_logic;
        D: in std_logic_vector(3 downto 0);
        Wreg: out std_logic;
        Q: out std_logic_vector(3 downto 0);
        DAC: out std_logic
        );
    end component;

    constant MCLK_PERIOD : time := 20 ns;
    signal clk_tb: std_logic;
    signal reset_tb: std_logic;
    signal CTS_tb: std_logic;
    signal DAV_tb: std_logic;
    signal D_tb: std_logic_vector(3 downto 0);
    signal Wreg_tb: std_logic;
    signal Q_tb: std_logic_vector(3 downto 0);
    signal DAC_tb: std_logic;
begin
    URingBuffer: RingBuffer
        port map(
        clk  => clk_tb,
        reset => reset_tb,
        CTS => CTS_tb,
        DAV => DAV_tb,
        D => D_tb,
        Wreg => Wreg_tb,
        Q => Q_tb,
        DAC => DAC_tb
        );

    p_CLK_gen: process
    begin
        while true loop
            clk_tb <= '0';
            wait for MCLK_PERIOD/2;
            clk_tb <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;

    stimulus: process
    begin
        reset_tb <= '1';
        wait for 2 * MCLK_PERIOD;
        reset_tb <= '0';
        wait for 2 * MCLK_PERIOD;

        D_tb <= "1001";
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;

        D_tb <= "1111";
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '1';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '0';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '1';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '0';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '1';
        wait for 2*MCLK_PERIOD;
        CTS_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '1';
        wait for 2*MCLK_PERIOD;
        DAV_tb <= '0';
        wait;
    end process;
end test;
