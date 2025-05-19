library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity KeyboardReader_tb is
end KeyboardReader_tb;

architecture behavioral of KeyboardReader_tb is

    component KeyboardReader
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            COL: out std_logic_vector(3 downto 0);
            ACK: in std_logic;
            D: out std_logic_vector(3 downto 0);
            Dval: out std_logic
        );
    end component;

    constant MCLK_PERIOD: time := 20 ns;
    constant MCLK_HALF_PERIOD: time := MCLK_PERIOD / 2;

    signal CLK_tb: std_logic;
    signal RESET_tb: std_logic;
    signal LIN_tb: std_logic_vector(3 downto 0);
    signal COL_tb: std_logic_vector(3 downto 0);
    signal ACK_tb: std_logic;
    signal D_tb: std_logic_vector(3 downto 0);
    signal Dval_tb: std_logic;

begin

    test: KeyboardReader port map(
        CLK => CLK_tb,
        RESET => RESET_tb,
        LIN => LIN_tb,
        COL => COL_tb,
        ACK => ACK_tb,
        D => D_tb,
        Dval => Dval_tb
    );

    CLK_gen: process
    begin
        while true loop
            CLK_tb <= '0';
            wait for MCLK_HALF_PERIOD;
            CLK_tb <= '1';
            wait for MCLK_HALF_PERIOD;
        end loop;
    end process;

    stimulus: process
        variable lin_0: std_logic_vector(3 downto 0) := "1110";
        variable lin_1: std_logic_vector(3 downto 0) := "1101";
        variable lin_2: std_logic_vector(3 downto 0) := "1011";
        variable lin_3: std_logic_vector(3 downto 0) := "0111";
    begin
        ACK_tb <= '0';
        RESET_tb <= '1';
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;

        RESET_tb <= '0';
        wait for MCLK_PERIOD * 2;
        -----------------------------------------------------------
        -- Inserir a tecla e o control consome instantaneamente
        -----------------------------------------------------------
        -- tecla 1
        wait until COL_tb = "1110";
        LIN_tb <= lin_0;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;
        
        --esta tecla não pode ser consumida, pois o keyDecode ainda n recebeu o ACK do RingBuffer
        LIN_tb <= lin_3; 
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;

        wait until Dval_tb = '1';
        assert D_tb = "0000" report "Error: Expected D = 0000" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        wait for MCLK_PERIOD;
        -- tecla 2
        wait until COL_tb = "1101";
        LIN_tb <= lin_0;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait until Dval_tb = '1';
        assert D_tb = "0001" report "Error: Expected D = 0001" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        wait for MCLK_PERIOD;
        -- tecla 3
        wait until COL_tb = "1011";
        LIN_tb <= lin_0;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait until Dval_tb = '1';
        assert D_tb = "0010" report "Error: Expected D = 0010" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        wait for MCLK_PERIOD;
        -- tecla A
        wait until COL_tb = "0111";
        LIN_tb <= lin_0;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait until Dval_tb = '1';
        assert D_tb = "0011" report "Error: Expected D = 0011" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for 2*MCLK_PERIOD;
        ----------------------------------------------------------------------------------
        -- inseriri varias teclas sem serem consumidas
        ----------------------------------------------------------------------------------
        -- tecla 4
        wait until COL_tb = "1110";
        LIN_tb <= lin_1;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait for MCLK_PERIOD;
        -- tecla 5
        wait until COL_tb = "1101";
        LIN_tb <= lin_1;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait for MCLK_PERIOD;
        -- tecla 6
        wait until COL_tb = "1011";
        LIN_tb <= lin_1;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait for MCLK_PERIOD;
        -- tecla B
        wait until COL_tb = "0111";
        LIN_tb <= lin_1;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        wait for MCLK_PERIOD;
        --------------------------------------------------------------------------
        -- Consumir as teclas armazenadas no ringbuffer
        --------------------------------------------------------------------------
        assert Dval_tb = '1' report "Error: Expected Dval = 1" severity failure;
        assert D_tb = "0100" report "Error: Expected D = 0100" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        wait until Dval_tb = '1';
        assert D_tb = "0101" report "Error: Expected D = 0101" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for MCLK_PERIOD;

        -- insere uma tecla enquanto o control consome outras.
        -- tecla D, é esperado que seja a ultima a ser libertada da memoria
        wait until COL_tb = "0111";
        LIN_tb <= lin_3;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";
        wait for MCLK_PERIOD;

        --wait until Dval_tb = '1';
        assert Dval_tb = '1' report "Error: Expected Dval = 1" severity failure;
        assert D_tb = "0110" report "Error: Expected D = 0110" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        wait until Dval_tb = '1';
        assert D_tb = "0111" report "Error: Expected D = 0111" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for MCLK_PERIOD;

        -- insiro tecla 9
        wait until COL_tb = "1011";
        LIN_tb <= lin_2;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        -- insiro a tecla 8
        wait until COL_tb = "1101";
        LIN_tb <= lin_2;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        -- control consume a tecla D
        --wait until Dval_tb = '1';
        assert Dval_tb = '1' report "Error: Expected Dval = 1" severity failure;
        assert D_tb = "1111" report "Error: Expected D = 1111" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for MCLK_PERIOD;

        --insiro a tecla 0
        wait until COL_tb = "1101";
        LIN_tb <= lin_3;
        wait for MCLK_PERIOD;
        LIN_tb <= "1111";

        -- consome a tecla 9
        assert Dval_tb = '1' report "Error: Expected Dval = 1" severity failure;
        assert D_tb = "1010" report "Error: Expected D = 1010" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        -- consome a tecla 8
        wait until Dval_tb = '1';
        assert Dval_tb = '1' report "Error: Expected Dval = 1" severity failure;
        assert D_tb = "1001" report "Error: Expected D = 1001" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';

        -- consome a tecla 0
        wait until Dval_tb = '1';
        assert D_tb = "1101" report "Error: Expected D = 1101" severity failure;
        wait for MCLK_PERIOD;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        wait for MCLK_PERIOD;

        -- tentar consumir com a memoria fazia
        assert Dval_tb = '0' report "Error: Expected Dval = 0" severity failure;
        ACK_tb <= '1';
        wait for MCLK_PERIOD;
        ACK_tb <= '0';
        assert Dval_tb = '0' report "Error: Expected Dval = 0" severity failure;
        -- tem que ser igual à ultima tecla consumida no caso 0
        assert D_tb = "1101" report "Error: Expected D = 1101" severity failure;
        wait;
    end process;

end behavioral;