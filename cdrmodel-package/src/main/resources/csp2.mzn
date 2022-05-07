% An example for the paper

% Conflict sets: {c1, c2}, {c2, c4}, {c3, c6}
% Diagnosis sets: {c2, c3}, {c2, c6}, {c1, c3, c4}, {c1, c4, c6}
% Diagnosis sets (MSS-based): {c5, c6}, {c2, c6}, {c4, c5, c7}, {c2, c4, c7}
%                             {c3, c2}, {c6, c2}, {c4, c3, c1}, {c6, c4, c1}

% v1, v2, v3 = [1, 2, 3]
% [variable] [operator] [variable/value]
constraint v2 == 1;
constraint v2 > 2;
constraint v3 == 1;
constraint v2 < 2;
constraint v1 > 2;
constraint v3 > 2;
constraint v1 > 1;