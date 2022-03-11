# Notes

## TODO

- Probably pretty important but, make a TODO.

## Flow

### Auto

- Shoot balls (2)
- Drive bakcwards (2 sec)
  
### Auto (fancy)

- Shoot balls (2)
- Use VP to find other balls
- Shoot them

### Teleop

- Tank drive
- Climbing
- Aiming & shooting (manual)
- Get balls

### Teleop (fancy)

- Tank/mecanum drive
- Climing
- Aiming & shooting via formula

## Hardware

IDs are placeholders. Fix that.

### Drive motors

- 0: Drive mootr #1 (FL)
- 1: Drive motor #2 (FR)
- 2: Drive motor #3 (BL)
- 3: Drive motor #4 (BR)

### Turret motors

- 4: Turret rotate motor #1 *
- 5: Turret shoot->aim motor #1 *
- 6: Turret shoot->aim motor #2 *
- 7: Turret shoot->shoot motor #1
- 8: Turret shoot->shoot motor #2

### Cargo motor

- 9: Cargo ball motor #1 *

### Climb motor

- 10: Climb motor #1
- 11: Climb motor #2

### Switches

- 0: Shoot->aim #1
- 1: Shoot->aim #2
- 2: Hub rotation #1
- 3: Hub rotation #2
- 4: Cargo detection #1

### Potentiometer

- 0: Shoot->aim #1

### Cameras

- 0: Turret camera #1
- 1: Front camera #1

### Jetson

- 0: Jetson TX-1 #1

## How Things Work

### Climbing

- Get lined up
- Get lined up (color sensor, gyro in fancy mode)
- 2 motors, when they are going we are climbing
- Pneumatics to grip (possibly)

### Aiming & shooting

- Susan is lazy
- Rotation of turret base is on one motor
- Rotation of turret angle is on some other motor
- Wheel in fron that we spin up
- 2 motors power the wheel that sends the ball flying
- Ball goes up bot, gets gripped by wheel
- Ball goes flying

### Aiming & shooting (notes)

- Focus on angle rather than wheel speed

### Sucking balls

- Go up to the ball
- Arm comes down with grippy wheels
- Wheels spin, ball goes straight into bot
- Ball gets automatically shot
- Manual shooting may suck dick

## Random

### Potentiometers

- Analog
- 0: 0 deg
- 5: 300 deg
- Diff between 0V & 5V

### Limit switches

- Digital

### Climbing

- Motors off wihle pistons on
- Grab w/ back piston
- Run motors
- Grab w/ front piston
- Release back piston
- Run motors until we can grab again w/ hook
- Release front piston
- Hang there