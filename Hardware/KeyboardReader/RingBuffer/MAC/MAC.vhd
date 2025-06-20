library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity MAC is
    Port (
        clk: in std_logic;
        reset: in std_logic;
        incPut: in std_logic;
        incGet: in std_logic;
        PUTGET: in std_logic;
        ADDR: out std_logic_vector(3 downto 0);
        full: out std_logic;
        empty: out std_logic
    );
end MAC;

architecture arc_MAC of MAC is

component ADDER4 is
    port(
        A: in std_logic_vector (3 downto 0);
        B: in std_logic_vector (3 downto 0);
        CIN: in std_logic;
        S: out std_logic_vector (3 downto 0)
    );
end component;

component Reg4 is
    port(
		CLK: in std_logic;
		RESET: in std_logic;
		SET: in std_logic;
		D: in std_logic_vector(3 downto 0);
		EN: in std_logic;
		Q: out std_logic_vector(3 downto 0)
    );
end component;

component MUX_4 is
    port(
		Se: in std_logic;
		A: in std_logic_vector(3 downto 0);
		B: in std_logic_vector(3 downto 0);
		OUTPUT: out std_logic_vector(3 downto 0)
    );
end component;

component Equality is
    port(
        A: in std_logic_vector(3 downto 0);
        B: in std_logic_vector(3 downto 0);
        O: out std_logic
    );
end component;

signal adder_out, mux_out, putReg_out, getReg_out: std_logic_vector (3 downto 0);
signal equ : std_logic;
signal count: unsigned(4 downto 0);

begin

UADDER4: ADDER4 port map(
    A => mux_out,
    B => "0000",
    CIN => '1',
    S => adder_out
);

PUT_REG4: Reg4 port map(
        CLK => clk,
		RESET => reset,
		SET => '0',
		D => adder_out,
		EN => incPut,
		Q => putReg_out
);

GET_REG4: Reg4 port map(
        CLK => clk,
		RESET => reset,
		SET => '0',
		D => adder_out,
		EN => incGet,
		Q => getReg_out
);

UMUX_4: MUX_4 port map(
    Se => PUTGET,
    A => putREG_out,
    B => getREG_out,
    OUTPUT => mux_out
);

UEQ: Equality port map(
    A => putReg_out,
    B => getReg_out,
    O => equ
);

process(clk, reset)
begin
    if reset = '1' then
        count <= (others => '0');
    elsif rising_edge(clk) then
        if incPut = '1' and incGet = '0' and count < "10000" then
            count <= count + 1;
        elsif incGet = '1' and incPut = '0' and count > "00000" then
            count <= count - 1;
        end if;
    end if;
end process;

full <= '1' when count = "10000" else '0';
empty <= '1' when count = "00000" else '0';

ADDR <= mux_out;
end arc_MAC;