library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity SerialReceiver_tb is
end SerialReceiver_tb;

architecture test of tb_SerialReceiver is
    component SerialReceiver is
        port(
            SDX: in  std_logic;
            SCLK: in  std_logic;
            SS: in  std_logic;
            accept: in  std_logic;
            RESET: in  std_logic;
            D: out std_logic_vector(4 downto 0);
            DX_val: out std_logic
        );
    end component;

    signal s_SDX     : std_logic := '0';
    signal s_SCLK    : std_logic := '0';
    signal s_SS      : std_logic := '0';
    signal s_accept  : std_logic := '0';
    signal s_RESET   : std_logic := '0';
    signal s_D       : std_logic_vector(4 downto 0);
    signal s_DX_val  : std_logic;

    constant MCLK_PERIOD : time := 20 ns;

begin
    UUT: SerialReceiver
        port map(
            SDX => s_SDX,
            SCLK => s_SCLK,
            SS => s_SS,
            accept => s_accept,
            RESET => s_RESET,
            D => s_D,
            DX_val => s_DX_val
        );

    p_CLK_gen: process
    begin
        while true loop
            s_SCLK <= '0';
            wait for MCLK_PERIOD/2;
            s_SCLK <= '1';
            wait for MCLK_PERIOD/2;
        end loop;
    end process;

    stim_proc: process
    begin
        s_RESET <= '1';
        s_SS    <= '0';
        s_accept <= '0';
        wait for 5*MCLK_PERIOD;

        s_RESET <= '0';
        wait for 2*MCLK_PERIOD;

        s_SS <= '1';
        wait for 2*MCLK_PERIOD;


        s_SDX <= '1';
        wait for MCLK_PERIOD;
        s_SDX <= '0';
        wait for MCLK_PERIOD;
        s_SDX <= '1';
        wait for MCLK_PERIOD;
        s_SDX <= '0';
        wait for MCLK_PERIOD;
        s_SDX <= '1';
        wait for MCLK_PERIOD;


        -- Bit de paridade P (exemplo)
        s_SDX <= '0';
        wait for MCLK_PERIOD;


        wait for MCLK_PERIOD*2;

        s_accept <= '1';
        wait for MCLK_PERIOD;
        s_accept <= '0';
        wait for MCLK_PERIOD*2;

        s_SS <= '0';
        s_SDX <= '1';
        wait for MCLK_PERIOD*5;

        s_SS <= '1';
        wait for MCLK_PERIOD*2;


        s_SDX <= '0';
        wait for MCLK_PERIOD;
        s_SDX <= '1';
        wait for MCLK_PERIOD;
        s_SDX <= '0';
        wait for MCLK_PERIOD;
        s_SDX <= '1';
        wait for MCLK_PERIOD;
        s_SDX <= '0';
        wait for MCLK_PERIOD;

        -- Bit de paridade (P) errado (por exemplo '1' se deveria ser '0')
        s_SDX <= '1';
        wait for MCLK_PERIOD;

        -- Espera resultado
        wait for MCLK_PERIOD*4;

        wait;
    end process;

end test;
