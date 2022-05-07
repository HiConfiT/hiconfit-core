% Use this editor as a MiniZinc scratch book FlatZinc

% Conflict sets: {c1, c3}, {c2, c4}
% Diagnosis sets: {c1, c2}, {c1, c4}, {c2, c3}, {c3, c4}
% Diagnosis sets (MSS-based version): {c4, c3}, {c4, c1}, {c3, c2}, {c2, c1}
%                                     {c1, c2}, {c1, c4}, {c2, c3}, {c3, c4}

% x, y = [-10, 10]
% constraint [variable] [operator] [variable/value]
constraint x > 1;
constraint x < y;
constraint x < 1;
constraint y == -10;