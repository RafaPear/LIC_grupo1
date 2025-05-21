library ieee;
use ieee.std_logic_1164.all;

entity RouletteGame is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        K_LIN: in std_logic_vector(3 downto 0); -- KeyboardReader
        K_COL: out std_logic_vector(3 downto 0); -- KeyboardReader
        L_E: out std_logic; -- SLCDC
        L_Dout: out std_logic_vector(4 downto 0); -- SLCDC
        HEX0, HEX1, HEX2, HEX3, HEX4, HEX5	: out std_logic_vector(7 downto 0)
    );
end RouletteGame;

architecture arch_RouletteGame of RouletteGame is
    component UsbPort is
        port(
            inputPort : in std_logic_vector(7 downto 0);
            outputPort : out std_logic_vector(7 downto 0)
        );
    end component;

    component SLCDC is
        port(
            clk: in std_logic;
            RESET: in std_logic;
            LCDsel: in std_logic;
            SCLK: in std_logic;
            SDX: in std_logic;
            E: out std_logic;
            Dout: out std_logic_vector(4 downto 0)
        );
    end component;

    component SRC is
        port(
            clk: in std_logic;
            RESET: in std_logic;
            Rsel: in std_logic;
            SCLK: in std_logic;
            SDX: in std_logic;
            set: out std_logic;
            Dout: out std_logic_vector(7 downto 0)
        );
    end component;

    component KeyboardReader is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            LIN: in std_logic_vector(3 downto 0);
            ACK: in std_logic;
            COL: out std_logic_vector(3 downto 0);
            D: out std_logic_vector(3 downto 0);
            Dval: out std_logic
        );
    end component;

    component rouletteDisplay is
    port(	set	: in std_logic;
            cmd	: in std_logic_vector(2 downto 0);
            data	: in std_logic_vector(4 downto 0);
            HEX0	: out std_logic_vector(7 downto 0);
            HEX1	: out std_logic_vector(7 downto 0);
            HEX2	: out std_logic_vector(7 downto 0);
            HEX3	: out std_logic_vector(7 downto 0);
            HEX4	: out std_logic_vector(7 downto 0);
            HEX5	: out std_logic_vector(7 downto 0)
            );
    end component;
    
    signal D_SRC: std_logic_vector(7 downto 0);
    signal R_SET_temp: std_logic;

    -- Signals for USBPort
    signal temp_inPort: std_logic_vector(7 downto 0);
    signal temp_outPort: std_logic_vector(7 downto 0);

    -- Signals for KeyboardReader
    signal temp_K_D: std_logic_vector(3 downto 0);
    signal temp_K_ACK: std_logic := '0'; -- Atribuído valor default
    signal temp_K_Dval: std_logic;

    -- Signals for SLCDC
    signal temp_L_LCDsel: std_logic := '0';
    signal temp_L_SCLK: std_logic := '0';
    signal temp_L_SDX: std_logic := '0';

    -- Signals for SRC
    signal temp_R_Rsel: std_logic := '0';
    signal temp_R_SCLK: std_logic := '0';
    signal temp_R_SDX: std_logic := '0';

begin

    -- USBPort outport logic (ligação de teclado)
    temp_outPort(3 downto 0) <= temp_K_D;
    temp_outPort(4) <= temp_K_Dval;
    temp_outPort(7 downto 5) <= (others => '0'); -- Preencher restantes bits

    -- USBPort inport logic (evitar conflitos com OR)
    temp_inPort(0) <= temp_L_LCDsel;
    temp_inPort(1) <= temp_R_Rsel;
    temp_inPort(2) <= '0'; -- Reservado
    temp_inPort(3) <= temp_L_SDX or temp_R_SDX; -- Combinação lógica
    temp_inPort(4) <= temp_L_SCLK or temp_R_SCLK; -- Combinação lógica
    temp_inPort(5) <= '0';
    temp_inPort(6) <= '0';
    temp_inPort(7) <= temp_K_ACK;

    -- Instanciar UsbPort
    UsbPort_inst: UsbPort port map(
        inputPort => temp_inPort,
        outputPort => temp_outPort
    );

    -- Instanciar SLCDC
    SLCDC_inst: SLCDC port map(
        clk => CLK,
        RESET => RESET,
        LCDsel => temp_L_LCDsel,
        SCLK => temp_L_SCLK,
        SDX => temp_L_SDX,
        E => L_E,
        Dout => L_Dout
    );

    -- Instanciar SRC
    SRC_inst: SRC port map(
        clk => CLK,
        RESET => RESET,
        Rsel => temp_R_Rsel,
        SCLK => temp_R_SCLK,
        SDX => temp_R_SDX,
        set => R_SET_temp,
        Dout => D_SRC
    );

    RouleteDisplay_inst: rouletteDisplay port map(
        set => R_SET_temp,
        cmd => D_SRC(2 downto 0),
        data => D_SRC(7 downto 3),
        HEX0 => HEX0,
        HEX1 => HEX1,
        HEX2 => HEX2,
        HEX3 => HEX3,
        HEX4 => HEX4,
        HEX5 => HEX5
    );

    -- Instanciar KeyboardReader
    KeyboardReader_inst: KeyboardReader port map(
        CLK => CLK,
        RESET => RESET,
        LIN => K_LIN,
        ACK => temp_K_ACK,
        COL => K_COL,
        D => temp_K_D,
        Dval => temp_K_Dval
    );

end arch_RouletteGame;
