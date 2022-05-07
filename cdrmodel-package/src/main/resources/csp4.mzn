% An example for the paper

% Conflict sets: {c1, c2}
% Diagnosis sets: {c1}, {c2}
% Diagnosis sets (MSS-based): {c7}, {c6}

% v1, v2, v3 = [1, 2, 3]
% [variable] [operator] [variable/value]
constraint v2 == 1;
constraint v2 > 2;
constraint v3 == 1;
constraint v1 == 2;
constraint v1 >= 2;
constraint v3 > 0;
constraint v1 <= 2;