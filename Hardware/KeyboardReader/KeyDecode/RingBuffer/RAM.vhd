library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_arith.all;

entity RAM is
	generic(
		ADDRESS_WIDTH : natural := 3;
		DATA_WIDTH : natural := 4
	);
	port(
		address : in std_logic_vector(ADDRESS_WIDTH - 1 downto 0);
		wr: in std_logic;
		din: in std_logic_vector(DATA_WIDTH - 1 downto 0);
		dout: out std_logic_vector(DATA_WIDTH - 1 downto 0)
	);
end RAM;

architecture behavioral of RAM is

	type RAM_TYPE is array (0 to 2**ADDRESS_WIDTH - 1) of std_logic_vector(DATA_WIDTH - 1 downto 0);
	signal ram : RAM_TYPE;

begin

	process(address, wr)
	begin
		dout <= ram(conv_integer(unsigned(address)));

		if (wr='1') then
			ram(conv_integer(unsigned(address))) <= din;
		end if;
	end process;

end behavioral;