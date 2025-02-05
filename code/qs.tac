i = m - 1
j = n
t_1 = 4 * n
v = a[t_1]
i = i + 1
t_2 = 4 * i
t_3 = a[t_2]
if t_3<v goto 4
j = j - 1
t_4 = 4 * j
t_5 = a[t_4]
if t_5>v goto 8
if i>=j goto 22
t_6 = 4 * i
x = a[t_6]
t_7 = 4 * i
t_8 = 4 * j
t_9 = a[t_8]
a[t_7] = t_9
t_10 = 4 * j
a[t_10] = x
goto 4
t_11 = 4 * i
x = a[t_11]
t_12 = 4 * i
t_13 = 4 * n
t_14 = a[t_13]
a[t_12] = t_14
t_15 = 4 * n
a[t_15] = x
