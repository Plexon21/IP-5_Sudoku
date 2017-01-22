%% Setup der Daten für den Classifier

% Es kann zwischen log und normierten daten umgeschaltet werden, 
% indem die Kommentare angepasst werden



clc;
load('__old.mat');

% Aufbau Input Matrix
% 1: Difficulty
% 2: wasSolved
% 3: NakedSingles
% 4: HiddenSingles
% 5: NakedSubsets_Size2
% 6: HiddenSubsets_Size2
% 7: BlockLine Interactions
% 8: NakedSubsets_Size3
% 9: HiddenSubsets_Size3
% 10: NakedSubsets_Size4
% 11: HiddenSubsets_Size4
% 12: XWing
% 13: GivenCount
% 14: Anz Start 1
% 15: Anz Start 2
% 16: Anz Start 3
% 17: Anz Start 4
% 18: Anz Start 5
% 19: Anz Start 6
% 20: Anz Start 7
% 21: Anz Start 8
% 22: Anz Start 9
% 23: Anz Possibilities
% 24: wasBacktracked

label_size = 7; 

toSolveCount = (81-shuffledInput_old(:,8));
percentageNakedSingles = shuffledInput_old(:,3)./toSolveCount*100;
percentageHiddenSingles = shuffledInput_old(:,4)./toSolveCount*100;
%toSolveCount2 = (81-Statistics(:,8));
%percentageNakedSingles2 = Statistics(:,3)./toSolveCount2*100;
%percentageHiddenSingles2 = Statistics(:,4)./toSolveCount2*100;


% Aufbau Features
% 1: % Naked Singles von zu lösenden Zahlen
% 2: % Hidden Singles von zu lösenden Zahlen
% 3: NakedSubsets_Size2
% 4: HiddenSubsets_Size2
% 5: BlockLine Interactions
% 6: NakedSubsets_Size3
% 7: HiddenSubsets_Size3
% 8: NakedSubsets_Size4
% 9: HiddenSubsets_Size4
% 10: XWing
% 11: GivenCount
% 12: Anz Start 1
% 13: Anz Start 2
% 14: Anz Start 3
% 15: Anz Start 4
% 16: Anz Start 5
% 17: Anz Start 6
% 18: Anz Start 7
% 19: Anz Start 8
% 20: Anz Start 9
% 21: Anz Possibilities
% 22: wasBacktracked

%X = normc([percentageNakedSingles percentageHiddenSingles shuffledInput_old(:,5) shuffledInput_old(:,6) shuffledInput_old(:,7) shuffledInput_old(:,8) shuffledInput_old(:,9) shuffledInput_old(:,10) shuffledInput_old(:,11) shuffledInput_old(:,12) shuffledInput_old(:,13) shuffledInput_old(:,14) shuffledInput_old(:,15) shuffledInput_old(:,16) shuffledInput_old(:,17) shuffledInput_old(:,18) shuffledInput_old(:,19) shuffledInput_old(:,20) shuffledInput_old(:,21) shuffledInput_old(:,22) shuffledInput_old(:,23) shuffledInput_old(:,24)]);
X = log([percentageNakedSingles+1 percentageHiddenSingles+1 shuffledInput_old(:,5)+1 shuffledInput_old(:,6)+1 shuffledInput_old(:,7)+1 shuffledInput_old(:,8)+1 shuffledInput_old(:,9)+1 shuffledInput_old(:,10)+1 shuffledInput_old(:,11)+1 shuffledInput_old(:,12)+1 shuffledInput_old(:,13)+1 shuffledInput_old(:,14)+1 shuffledInput_old(:,15)+1 shuffledInput_old(:,16)+1 shuffledInput_old(:,17)+1 shuffledInput_old(:,18)+1 shuffledInput_old(:,19)+1 shuffledInput_old(:,20)+1 shuffledInput_old(:,21)+1 shuffledInput_old(:,22)+1 shuffledInput_old(:,23)+1 shuffledInput_old(:,24)+1]);

%XTest = normc([percentageNakedSingles2 percentageHiddenSingles2 Statistics(:,5) Statistics(:,6) Statistics(:,7) Statistics(:,8) Statistics(:,9) Statistics(:,10) Statistics(:,11) Statistics(:,12) Statistics(:,13) Statistics(:,14) Statistics(:,15) Statistics(:,16) Statistics(:,17) Statistics(:,18) Statistics(:,19) Statistics(:,20) Statistics(:,21) Statistics(:,22) Statistics(:,23) Statistics(:,24)]);

y = oneHot(shuffledInput_old(:,1));

training_size = floor(size(X,1)*0.8);





%load('__kti.mat');


%toSolveCount = (81-shuffledInput(:,8));
%percentageNakedSingles = shuffledInput(:,3)./toSolveCount*100;
%percentageHiddenSingles = shuffledInput(:,4)./toSolveCount*100;

%XVal = normc([percentageNakedSingles percentageHiddenSingles shuffledInput(:,5) shuffledInput(:,6) shuffledInput(:,7) shuffledInput(:,8) shuffledInput(:,9) shuffledInput(:,10) shuffledInput(:,11) shuffledInput(:,12) shuffledInput(:,13) shuffledInput(:,14) shuffledInput(:,15) shuffledInput(:,16) shuffledInput(:,17) shuffledInput(:,18) shuffledInput(:,19) shuffledInput(:,20) shuffledInput(:,21) shuffledInput(:,22) shuffledInput(:,23) shuffledInput(:,24)]);


%yVal = oneHot(shuffledInput(:,1));






XTrain = X(1:training_size,:);
XVal =X(training_size+1:end,:);

yTrain = y(1:training_size,:);
yVal = y(training_size+1:end,:);

allTrain = [XTrain yTrain];

%% Classify with generated neural network

y_resNN = test_generated_trainOld_log(XVal);

size(y_resNN)
size(yVal)

fprintf('\nTraining Set Accuracy: %f\n', mean(double(y_resNN == yVal)) * 100);

plotconfusion(yVal',y_resNN');

%% Classify new generated sudokus
load('generated/randomflipped.mat');
label_size = 7; 

toSolveCount = (81-Generated(:,8));
percentageNakedSingles = Generated(:,3)./toSolveCount*100;
percentageHiddenSingles = Generated(:,4)./toSolveCount*100;

%X = normc([percentageNakedSingles percentageHiddenSingles Generated(:,5) Generated(:,6) Generated(:,7) Generated(:,8) Generated(:,9) Generated(:,10) Generated(:,11) Generated(:,12) Generated(:,13) Generated(:,14) Generated(:,15) Generated(:,16) Generated(:,17) Generated(:,18) Generated(:,19) Generated(:,20) Generated(:,21) Generated(:,22) Generated(:,23) Generated(:,24)]);

X = log([percentageNakedSingles+1 percentageHiddenSingles+1 Generated(:,5)+1 Generated(:,6)+1 Generated(:,7)+1 Generated(:,8)+1 Generated(:,9)+1 Generated(:,10)+1 Generated(:,11)+1 Generated(:,12)+1 Generated(:,13)+1 Generated(:,14)+1 Generated(:,15)+1 Generated(:,16)+1 Generated(:,17)+1 Generated(:,18)+1 Generated(:,19)+1 Generated(:,20)+1 Generated(:,21)+1 Generated(:,22)+1 Generated(:,23)+1 Generated(:,24)+1]);


%calculations using generated Methods (generated by Neural Fitting App)
%y_res = test_generated_trainOld(X)
y_res = test_generated_trainOld_log(X);

[~,y_resMax] = max(y_res,[],2);

veryeasy = length(find(y_resMax==1))
easy = length(find(y_resMax==2))
medium = length(find(y_resMax==3))
hard = length(find(y_resMax==4))
veryhard = length(find(y_resMax==5))
veryhardexpert = length(find(y_resMax==6))
evil = length(find(y_resMax==7))

results = [find(X(:,1)) y_resMax];

