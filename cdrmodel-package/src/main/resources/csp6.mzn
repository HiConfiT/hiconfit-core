% An example for the paper

% Conflict sets: {c1, c2, c5}, {c1, c2, c7}, {c5, c7}
% Diagnosis sets: {c2, c5}, {c2, c7}, {c1, c4, c5}, {c1, c4, c7}
% Diagnosis sets (MSS-based): {c3, c6}, {c1, c6}, {c3, c4, c7}, {c1, c4, c7}
%                             {c5, c2}, {c7, c2}, {c5, c4, c1}, {c7, c4, c1}

% v1, v2, v3 = [1, 2, 3]
% [variable] [operator] [variable/value]
constraint v2 == v1;
constraint v2 < 2;
constraint v3 == 1;
constraint v3 >= 1;
constraint v1 > 2;
constraint v3 > 0;
constraint v1 == 2;