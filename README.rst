LIFX Switch
===========
LIFX Switch is an attempt to create a program that attempts to make it easier
and more convenient to switch LIFX lights on and off from a mobile phone.

It tries to monitor the state of the light, making it possible to make changes
to brightness and temperature without changing the colour.

Known bugs
----------
#. Selecting light while it is still displayed as "Unknown" will crash
   application.
#. Under some circumstances, turning light on will mark light as on, when
   it is off.
#. Making changes to light colour from other devices will not update state
   except on random occasions.

Todo
----
Still need to decide which of these are in scope for this project.

#. Lock screen widget with predefined buttons that automatically adjust
   lights to preset values.
#. Support light tags.
#. Alarm functionality.
#. Automatically temperature based on time of day.
#. Possible improvements to LIFX API, e.g. being able to change brightness and
   temperature independantly of other values.

Copyright
---------
Copyright 2014 Brian May

This file is part of LIFX Switch.

LIFX Switch is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

LIFX Switch is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LIFX Switch  If not, see <http://www.gnu.org/licenses/>.
