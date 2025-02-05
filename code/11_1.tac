x = n
y = x - 1
res = 0
if x != 1 goto 10
if x == 1 goto 7
y = y - 1
goto 3
res = res + y
x = x / y
goto 3
res = res
