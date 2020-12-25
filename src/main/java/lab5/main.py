import re

from pip._vendor.colorama import Fore, Style
from prettytable import PrettyTable

from simplexmethod import SimplexMethod


def open_file():
    try:
        return open("C:/Users/Serhii/IdeaProjects/Decision Theory/src/main/resources/lab5/input.txt")
    except FileNotFoundError:
        print("File does not exist")
        exit()


def get_matrix_table(matrix):
    if len(matrix) < 1: return ''

    x = PrettyTable()
    fields = ['']
    for i in range(len(matrix[0])):
        fields.append("B" + str(i + 1))

    x.field_names = fields
    for i in range(len(matrix)):
        x.add_row(['A' + str(i + 1)] + matrix[i])

    return x


def check_saddle_point(min_a, max_b):
    max_from_matrix_a = min_a[max(min_a, key=min_a.get)]
    min_from_matrix_b = max_b[min(max_b, key=max_b.get)]
    return [max_from_matrix_a, min_from_matrix_b]


def check_rows(first_row, second_row):
    equalElements = 0
    for i in range(len(first_row)):
        if first_row[i] < second_row[i]: return 0
        if first_row[i] == second_row[i]: equalElements += equalElements

    return 0 if equalElements == len(first_row) else 1


def check_dominant_rows(current_matrix):
    matrix_after_excluding_rows = []
    deleted_rows = []

    for i in range(len(current_matrix)):
        for j in range(len(current_matrix)):
            if i == j: continue
            result = check_rows(current_matrix[i], current_matrix[j])
            if result != 0 and j not in deleted_rows:
                deleted_rows.append(j)
                print("Strategy A" + str(i + 1), "dominant over strategy А" + str(j + 1))
                print("\nThat's why we remove the line: ", j + 1)

    for i in range(len(current_matrix)):
        if i in deleted_rows: continue
        matrix_after_excluding_rows.append(current_matrix[i])

    return matrix_after_excluding_rows


def check_dominant_columns(current_matrix):
    matrix_after_excluding_columns = []
    deleted_columns = []
    transposed_matrix = [list(x) for x in zip(*current_matrix)]

    for i in range(len(transposed_matrix)):
        for j in range(len(transposed_matrix)):
            if i == j: continue
            result = check_rows(transposed_matrix[j], transposed_matrix[i])
            if result != 0 and j not in deleted_columns:
                deleted_columns.append(j)
                print("Strategy B" + str(i + 1), "dominant over strategy B" + str(j + 1))
                print("\nThat's why we remove the column: ", j + 1)

    for i in range(len(current_matrix)):
        matrix_after_excluding_columns.append([])
        for j in range(len(current_matrix[i])):
            if j in deleted_columns: continue
            matrix_after_excluding_columns[i].append(current_matrix[i][j])
    return matrix_after_excluding_columns


file = open_file()

matrix = []
for line in file:
    matrix.append([int(d) for d in re.split(' ', re.sub('\n', '', line))])

if len(matrix) < 2: exit()
print(Fore.RED, "Input data", Style.RESET_ALL)
print(get_matrix_table(matrix))

print("\nCheck if matrix has saddle point\n", Style.RESET_ALL)
x = PrettyTable()
fields = ['']
for i in range(len(matrix)):
    fields.append("B" + str(i + 1))

fields.append('min(Ai)')
x.field_names = fields

minA = {}
maxB = {}

for i in range(len(matrix)):
    minA[i] = min(matrix[i])
    x.add_row(['A' + str(i + 1)] + matrix[i] + [Fore.RED + str(minA[i]) + Style.RESET_ALL])
    for j in range(len(matrix[i])):
        if j not in maxB or maxB[j] < matrix[i][j]:
            maxB[j] = matrix[i][j]

x.add_row(['max(Bi)' + Fore.RED] + [maxB[element] for element in maxB] + ['' + Style.RESET_ALL])
print(x)

[max_from_matrix_a, min_from_matrix_b] = check_saddle_point(minA, maxB)
if max_from_matrix_a == min_from_matrix_b:
    print("There is a saddle point!")
else:
    print("a = max(min(Ai)) =", max_from_matrix_a)
    print("b = min(max(Bi)) =", min_from_matrix_b)
    print("There is no saddle point as \"a != b\"")
    print("The price of the game is within", Fore.RED, max_from_matrix_a, "<= y <=", min_from_matrix_b, Style.RESET_ALL)

print("\nCheck if matrix has domain rows and columns:", Style.RESET_ALL)
print("\nFrom the standpoint of winning player A", Style.RESET_ALL)
matrix_after_excluding_rows = check_dominant_rows(matrix)
print(Fore.RED + "\nNew matrix view:", Style.RESET_ALL)
print(get_matrix_table(matrix_after_excluding_rows))

print("\nFrom the position of losing player B", Style.RESET_ALL)
matrix_after_excluding_columns = check_dominant_columns(matrix_after_excluding_rows)
print(Fore.RED + "\nNew matrix view:", Style.RESET_ALL)
print(get_matrix_table(matrix_after_excluding_columns))

transposed_matrix = [list(x) for x in zip(*matrix_after_excluding_columns)]
print("Lets find solution in mixed strategies", Style.RESET_ALL)
print("\n(Second player) Minimum function:", Style.RESET_ALL)

second_players_conditions = []
for i in range(len(transposed_matrix)):
    second_players_conditions.append('')
    for j in range(len(transposed_matrix[i])):
        second_players_conditions[i] += str(transposed_matrix[i][j]) + 'x_' + str(j + 1) + ' + '

for i in range(len(second_players_conditions)):
    print(second_players_conditions[i][:-2] + '>= 1')

main_condition = 'F(x) = '
for i in range(len(matrix_after_excluding_columns)):
    main_condition += 'x_' + str(i + 1) + ' + '

print(main_condition[:-2] + '--> min')

print("\n(First player) Minimum function:", Style.RESET_ALL)
first_players_conditions = []

vars_count = 0
for i in range(len(matrix_after_excluding_columns)):
    first_players_conditions.append('')
    columns = len(matrix_after_excluding_columns[i])
    for j in range(columns):
        first_players_conditions[i] += str(matrix_after_excluding_columns[i][j]) + 'y_' + str(j + 1) + ' + '
        if columns > vars_count:
            vars_count = columns

for i in range(len(first_players_conditions)):
    first_players_conditions[i] = first_players_conditions[i][:-2] + '<= 1'

main_condition = ''
for i in range(len(transposed_matrix)):
    main_condition += '1y_' + str(i + 1) + ' + '

main_condition = main_condition[:-2]

for line in first_players_conditions: print(line)
print('Z(y) = ' + main_condition + '--> max')

print(Fore.RED + "\n\nLets solve the task with simplex method.", Style.RESET_ALL)
print("\nThe objective function:", Fore.RED, main_condition + '--> max\n', Style.RESET_ALL)

simplex_result = SimplexMethod(num_vars=vars_count, constraints=first_players_conditions,
                               objective_function=main_condition)
print("\nResults: ", Style.RESET_ALL)

x_result = {}
y_result = {}
for key in simplex_result.solution:
    if 'y_' in key:
        y_result[key] = simplex_result.solution[key]
    elif 'x_' in key:
        x_result[key] = simplex_result.solution[key]

y_result_condition = 'F(y) = '
current_y_result = 0
for i in range(vars_count):
    print('y' + str(i + 1) + ' =', y_result['y_' + str(i + 1)], end=' ')
    current_y_result += 1 * y_result['y_' + str(i + 1)]
    y_result_condition += "1 * " + str(y_result['y_' + str(i + 1)]) + ' + '

print("\n" + Fore.RED + y_result_condition[:-2] + '= ' + str(current_y_result), Style.RESET_ALL, '\n')

x_result_condition = 'F(x) = '
current_x_result = 0
for i in range(vars_count):
    print('x' + str(i + 1) + ' =', x_result['x_' + str(i + 1)], end=' ')
    current_x_result += 1 * x_result['x_' + str(i + 1)]
    x_result_condition += "1 * " + str(x_result['x_' + str(i + 1)]) + ' + '

print("\n" + Fore.RED + x_result_condition[:-2] + '= ' + str(current_x_result), Style.RESET_ALL)

print("\nGame price = 1/F(x) = 1/(" + str(current_x_result) + ") =", str(1 / current_x_result))
