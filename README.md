# CustomEconomy

A lightweight economy plugin for Paper/Spigot 1.21.x servers with a simple balance and payment system.

## Features

- Player balance management
- Player-to-player payments
- Customizable currency symbol and formatting
- Configurable starting balance
- Fully customizable messages with color codes
- Sound effects for transactions
- Permission-based access control

## Requirements

- Java 21 or higher
- Paper/Spigot 1.21.x or higher
- Maven (for building from source)

## Installation

1. Download the latest `CustomEconomy-1.0.0.jar` from releases
2. Place the jar file in your server's `plugins` folder
3. Restart or reload your server
4. Configure the plugin by editing `plugins/CustomEconomy/config.yml`
5. Restart the server again to apply configuration changes

## Building from Source

```bash
mvn clean package
```

The compiled jar will be located in the `target` folder as `CustomEconomy-1.0.0.jar`.

## Configuration

The main configuration file is located at `plugins/CustomEconomy/config.yml`.

### Currency Settings

```yaml
currency:
  symbol: "$"
  decimals: true
```

- `symbol`: The currency symbol displayed before amounts (e.g., $, €, £, ⛃)
- `decimals`: Whether to show decimal places (true = $100.50, false = $100)

### Starting Balance

```yaml
start-balance: 100.0
```

The amount of money new players start with when they first join the server.

### Messages

All messages support Minecraft color codes using `&` (e.g., `&a` for green, `&c` for red).

```yaml
messages:
  prefix: "&7[&bCustomEconomy&7]&r "
  balance-self: "&aYour balance: &e{amount}"
  balance-others: "&a{player}'s balance: &e{amount}"
  player-not-found: "&cPlayer not found."
  invalid-amount: "&cPlease enter a valid amount greater than 0."
  no-self-pay: "&cYou cannot pay yourself."
  insufficient-funds: "&cYou don't have enough funds."
  paid-sender: "&aYou paid &e{player} &a{amount}. New balance: &e{newBalance}"
  paid-target: "&aYou received &e{amount} &afrom &e{player}. New balance: &e{newBalance}"
```

#### Available Placeholders

- `{amount}` - The transaction amount (formatted with currency symbol)
- `{player}` - The player's name
- `{newBalance}` - The updated balance after transaction (formatted with currency symbol)

#### Color Codes Reference

- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White
- `&r` - Reset

## Commands

### /balance [player]

Check your balance or another player's balance.

**Aliases:** `/bal`, `/money`

**Usage:**
- `/balance` - Check your own balance
- `/balance PlayerName` - Check another player's balance (requires permission)

**Examples:**
```
/balance
/bal Steve
/money Notch
```

### /pay <player> <amount>

Send money to another player.

**Usage:**
- `/pay <player> <amount>` - Pay the specified amount to a player

**Examples:**
```
/pay Steve 50
/pay Notch 100.50
/pay Alex 25
```

**Notes:**
- You cannot pay yourself
- You must have sufficient funds
- Amount must be greater than 0
- Both sender and recipient receive notifications
- Sound effects play on successful transactions

## Permissions

### customecon.balance

**Description:** Allows checking your own balance  
**Default:** `true` (all players)  
**Commands:** `/balance`

### customecon.balance.others

**Description:** Allows checking other players' balances  
**Default:** `op` (operators only)  
**Commands:** `/balance <player>`

### customecon.pay

**Description:** Allows paying other players  
**Default:** `true` (all players)  
**Commands:** `/pay`

## Data Storage

Player balances are stored in `plugins/CustomEconomy/balances.yml` in the following format:

```yaml
uuid-here: 150.0
another-uuid: 250.50
```

The file is automatically created on first run and saved when the server shuts down or the plugin is disabled.

## Support

For bug reports, feature requests, or questions, please open an issue on the project repository.

## License

This plugin is provided as-is without warranty. Feel free to modify and distribute according to your needs.

## Credits

Developed by raffymimi