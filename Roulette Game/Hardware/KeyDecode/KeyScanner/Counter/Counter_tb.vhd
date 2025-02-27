library ieee;
use ieee.std_logic_1164.all;

entity Counter_tb is
end Counter_tb;

architecture behavioral of Counter_tb is
    component Counter is
        port(
            RESET: in std_logic;
            CE: in std_logic;
            CLK: in std_logic;
            Q: out std_logic_vector(1 downto 0)
        );
    end component;
    
    -- UUT signals
    constant MCLK_PERIOD: time := 10 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal RESET_tb, CE_tb, CLK_tb: std_logic;
    signal Q_tb: std_logic_vector(1 downto 0);

begin

-- Unit Under Test
UUT: Counter port map(
    RESET => RESET_tb,
    CE => CE_tb,
    CLK => CLK_tb,
    Q => Q_tb
);

-- Clock process definitions
clk_gen: process
begin
    CLK_tb <= '0';
    wait for MCLK_HALF_PERIOD;
    CLK_tb <= '1';
    wait for MCLK_HALF_PERIOD;
end process;

-- Stimulus process
stimulus: process
begin 
    -- Reset Counter
    RESET_tb <= '1';
    CE_tb <= '0';
    wait for MCLK_PERIOD*2;

    -- Assert Counter
    Assert Q_tb = "00" report "Counter failed to initialize" 
    severity error;

    -- Enable Counter
    RESET_tb <= '0';
    CE_tb <= '1';
    wait for MCLK_PERIOD*2;

    wait;
end process;