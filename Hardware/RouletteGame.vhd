library ieee;
use ieee.std_logic_1164.all;

entity RouletteGame is
    port(
        CLK: in std_logic;
        RESET: in std_logic;
        L_E: out std_logic; -- SLCDC
        L_Dout: out std_logic_vector(4 downto 0); -- SLCDC
        K_LIN: in std_logic_vector(3 downto 0); -- KeyboardReader
        K_COL: out std_logic_vector(3 downto 0); -- KeyboardReader
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

    component rouletteDisplay is
        port(	
            set	: in std_logic;
            cmd	: in std_logic_vector(2 downto 0);
            data : in std_logic_vector(4 downto 0);
            HEX0 : out std_logic_vector(7 downto 0);
            HEX1 : out std_logic_vector(7 downto 0);
            HEX2 : out std_logic_vector(7 downto 0);
            HEX3 : out std_logic_vector(7 downto 0);
            HEX4 : out std_logic_vector(7 downto 0);
            HEX5 : out std_logic_vector(7 downto 0)
        );
    end component;

    
    component Reg8 is
        port(
            CLK: in std_logic;
            RESET: in std_logic;
            SET: in std_logic;
            D: in std_logic_vector(7 downto 0);
            EN: in std_logic;
            Q: out std_logic_vector(7 downto 0)
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

    signal temp_inputPort, temp_outputPort: std_logic_vector(7 downto 0);
    signal temp_LCDsel, temp_SCLK, temp_SDX: std_logic;
    signal temp_D: std_logic_vector(3 downto 0);
    signal temp_Dval, temp_ACK: std_logic;
    signal temp_RDsel, temp_RSCLK, temp_RSDX, temp_set: std_logic;
    signal temp_RDout: std_logic_vector(7 downto 0);
    signal temp_inReg: std_logic_vector(7 downto 0);

begin
    -- Instantiate UsbPort
    UsbPort_inst: UsbPort
        port map(
            inputPort => temp_inputPort,
            outputPort => temp_inReg
        );

    Reg8_inst: Reg8
        port map(
            CLK => CLK,
            RESET => RESET,
            SET => '0',
            D => temp_inReg,
            EN => '1', 
            Q => temp_outputPort
        );

    -- Instantiate SLCDC
    SLCDC_inst: SLCDC
        port map(
            clk => CLK,
            RESET => RESET,
            LCDsel => temp_LCDsel,
            SCLK => temp_SCLK,
            SDX => temp_SDX,
            E => L_E,
            Dout => L_Dout
        );

    SRC_inst: SRC
        port map(
            clk => CLK,
            RESET => RESET,
            Rsel => temp_RDsel,
            SCLK => temp_RSCLK,
            SDX => temp_RSDX,
            set => temp_set,
            Dout => temp_RDout
        );

    RouletteDisplay_inst: rouletteDisplay
        port map(
            set => temp_set,
            cmd => temp_RDout(2 downto 0),
            data => temp_RDout(7 downto 3),
            HEX0 => HEX0,
            HEX1 => HEX1,
            HEX2 => HEX2,
            HEX3 => HEX3,
            HEX4 => HEX4,
            HEX5 => HEX5
        );

    -- Instantiate KeyboardReader
    KeyboardReader_inst: KeyboardReader
        port map(
            CLK => CLK,
            RESET => RESET,
            LIN => K_LIN,
            ACK => temp_ACK,
            COL => K_COL,
            D => temp_D,
            Dval => temp_Dval
        );

    -- KeyboardReader
    temp_inputPort(3 downto 0) <= temp_D;
    temp_inputPort(4) <= temp_Dval;
    temp_ACK <= temp_outputPort(7);

    -- LCDCDC
    temp_LCDsel <= temp_outputPort(0);
    temp_SDX <= temp_outputPort(3);
    temp_SCLK <= temp_outputPort(4);

    -- SRC
    temp_RDsel <= temp_outputPort(1);
    temp_RSDX <= temp_outputPort(3);
    temp_RSCLK <= temp_outputPort(4);
end arch_RouletteGame;
