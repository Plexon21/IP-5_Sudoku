%analyseplots

%% Allgemeine Plots

% 1: wasSolved
% 2: NakedSingles
% 3: HiddenSingles
% 4: NakedSubset 2
% 5: HiddenSubset 2
% 6: BlockLineInteractions
% 7: NakedSubset 3
% 8: HiddenSubset 3
% 9: NakedSubset 4
% 10: HiddenSubset 4
% 11: XWing
% 12: GivenCount
% 13: Anzahl Start Pos 1
% 14: Anzahl Start Pos 2
% 15: Anzahl Start Pos 3
% 16: Anzahl Start Pos 4
% 17: Anzahl Start Pos 5
% 18: Anzahl Start Pos 6
% 19: Anzahl Start Pos 7
% 20: Anzahl Start Pos 8
% 21: Anzahl Start Pos 9
% 22: Anzahl Possibilities (Pencilmarks)
% 23: wasBacktracked
% 24: Difficulty

load('old_stats.mat');
%load('kti_stats.mat');

veryEasyIndex = find(Input(:,24)==1);
easyIndex = find(Input(:,24)==2);
mediumIndex = find(Input(:,24)==3);
hardIndex = find(Input(:,24)==4);
veryHardIndex = find(Input(:,24)==5);
evilIndex = find(Input(:,24)==6);
exoticIndex = find(Input(:,24)==7);

veryEasy = Input(veryEasyIndex,:);
easy = Input(easyIndex,:);
medium = Input(mediumIndex,:);
hard = Input(hardIndex,:);
veryHard = Input(veryHardIndex,:);
evil = Input(evilIndex,:);
exotic = Input(exoticIndex,:);

solvedVeryEasy = size(find(veryEasy(:,23)==0),1)/size(veryEasy,1)*100;
solvedEasy = size(find(easy(:,23)==0),1)/size(easy,1)*100;
solvedMedium = size(find(medium(:,23)==0),1)/size(medium,1)*100;
solvedHard = size(find(hard(:,23)==0),1)/size(hard,1)*100;
solvedVeryHard = size(find(veryHard(:,23)==0),1)/size(veryHard,1)*100;
solvedEvil = size(find(evil(:,23)==0),1)/size(evil,1)*100;
solvedExotic = size(find(exotic(:,23)==0),1)/size(exotic,1)*100;

Solved = [solvedVeryEasy; solvedEasy; solvedMedium; solvedHard;solvedVeryHard;solvedEvil;solvedExotic];

nakedSinglesVeryEasy = mean(veryEasy(:,2))/mean(81-veryEasy(:,12))*100
nakedSinglesEasy = mean(easy(:,2))/mean(81-easy(:,12))*100;
nakedSinglesMedium = mean(medium(:,2))/mean(81-medium(:,12))*100;
nakedSinglesHard = mean(hard(:,2))/mean(81-hard(:,12))*100;
nakedSinglesVeryHard = mean(veryHard(:,2))/mean(81-veryHard(:,12))*100;
nakedSinglesEvil = mean(evil(:,2))/mean(81-evil(:,12))*100;
nakedSinglesExotic = mean(exotic(:,2))/mean(81-exotic(:,12))*100;

hiddenSinglesVeryEasy = mean(veryEasy(:,3))/mean(81-veryEasy(:,12))*100;
hiddenSinglesEasy = mean(easy(:,3))/mean(81-easy(:,12))*100;
hiddenSinglesMedium = mean(medium(:,3))/mean(81-medium(:,12))*100;
hiddenSinglesHard = mean(hard(:,3))/mean(81-hard(:,12))*100;
hiddenSinglesVeryHard = mean(veryHard(:,3))/mean(81-veryHard(:,12))*100;
hiddenSinglesEvil = mean(evil(:,3))/mean(81-evil(:,12))*100;
hiddenSinglesExotic = mean(exotic(:,3))/mean(81-exotic(:,12))*100;

SolvingMethods = [[nakedSinglesVeryEasy;nakedSinglesEasy;nakedSinglesMedium;nakedSinglesHard;nakedSinglesVeryHard;nakedSinglesEvil;nakedSinglesExotic] [hiddenSinglesVeryEasy;hiddenSinglesEasy;hiddenSinglesMedium;hiddenSinglesHard;hiddenSinglesVeryHard;hiddenSinglesEvil;hiddenSinglesExotic]];

nakedSubsetsVeryEasy = mean(veryEasy(:,4));
nakedSubsetsEasy = mean(easy(:,4));
nakedSubsetsMedium = mean(medium(:,4));
nakedSubsetsHard = mean(hard(:,4));
nakedSubsetsVeryHard = mean(veryHard(:,4));
nakedSubsetsEvil = mean(evil(:,4));
nakedSubsetsExotic = mean(exotic(:,4));

hiddenSubsetsVeryEasy = mean(veryEasy(:,5));
hiddenSubsetsEasy = mean(easy(:,5));
hiddenSubsetsMedium = mean(medium(:,5));
hiddenSubsetsHard = mean(hard(:,5));
hiddenSubsetsVeryHard = mean(veryHard(:,5));
hiddenSubsetsEvil = mean(evil(:,5));
hiddenSubsetsExotic = mean(exotic(:,5));

Subsets = [[nakedSubsetsVeryEasy;nakedSubsetsEasy;nakedSubsetsMedium;nakedSubsetsHard;nakedSubsetsVeryHard;nakedSubsetsEvil;nakedSubsetsExotic] [hiddenSubsetsVeryEasy;hiddenSubsetsEasy;hiddenSubsetsMedium;hiddenSubsetsHard;hiddenSubsetsVeryHard;hiddenSubsetsEvil;hiddenSubsetsExotic]];

givenVeryEasy = mean(veryEasy(:,12));
givenEasy = mean(easy(:,12));
givenMedium = mean(medium(:,12));
givenHard = mean(hard(:,12));
givenVeryHard = mean(veryHard(:,12));
givenEvil = mean(evil(:,12));
givenExotic = mean(exotic(:,12));

Given = [givenVeryEasy;givenEasy;givenMedium;givenHard;givenVeryHard;givenEvil;givenExotic]

difficulties = {'very easy', 'easy', 'medium', 'hard', 'very hard', 'expert', 'exotic'};

figure;
bar1 = subplot(2,2,1);
bar(bar1,Solved);
set(gca,'xticklabel', difficulties);

bar2 = subplot(2,2,2);
bar(bar2,Given);
set(gca,'xticklabel', difficulties);

bar3 = subplot(2,2,3);
bar(bar3,SolvingMethods,'stacked');
methodBarNames = {'Naked Singles', 'Hidden Singles'};
set(gca,'xticklabel', difficulties);
legend(bar3,methodBarNames);

bar4 = subplot(2,2,4);
bar(bar4,Subsets);
subsetBarNames = {'Naked Pairs', 'Hidden Pairs'};
set(gca,'xticklabel', difficulties);
legend(bar4,subsetBarNames, 'Location', 'northwest');



title(bar1, 'Percentage of Sudokus solved');
title(bar2, 'Mean count of given Numbers');
title(bar3, 'Percentage of used Methods for solving');
title(bar4, 'Avg Nr. of used Subset-Methods for solving');


%% Histogramme Naked Singles
figure;

suptitle('Naked Singles');

hist1 = subplot(4,2,1);
histogram(veryEasy(:,2)./(81-veryEasy(:,12))*100,20);
title(hist1,'Very easy');
xlabel(hist1,'% of solving steps using Naked Singles');
ylabel(hist1,'Nr of Sudokus');

hist2 = subplot(4,2,2);
histogram(easy(:,2)./(81-easy(:,12))*100,20);
title(hist2,'Easy');
xlabel(hist2,'% of solving steps using Naked Singles');
ylabel(hist2,'Nr of Sudokus');

hist3 = subplot(4,2,3);
histogram(medium(:,2)./(81-medium(:,12))*100,20);
title(hist3,'Medium');
xlabel(hist3,'% of solving steps using Naked Singles');
ylabel(hist3,'Nr of Sudokus');

hist4 = subplot(4,2,4);
histogram(hard(:,2)./(81-hard(:,12))*100,20);
title(hist4,'Hard');
xlabel(hist4,'% of solving steps using Naked Singles');
ylabel(hist4,'Nr of Sudokus');

hist5 = subplot(4,2,5);
histogram(veryHard(:,2)./(81-veryHard(:,12))*100,20);
title(hist5,'Very Hard');
xlabel(hist5,'% of solving steps using Naked Singles');
ylabel(hist5,'Nr of Sudokus');

hist6 = subplot(4,2,6);
histogram(evil(:,2)./(81-evil(:,12))*100,20);
title(hist6,'Very Hard Expert');
xlabel(hist6,'% of solving steps using Naked Singles');
ylabel(hist6,'Nr of Sudokus');

hist7 = subplot(4,2,7);
histogram(exotic(:,3)./(81-exotic(:,12))*100,20);
title(hist7,'Evil&Exotic');
xlabel(hist7,'% of solving steps using Naked Singles');
ylabel(hist7,'Nr of Sudokus');

%% Histogramme Hidden Singles

figure;

suptitle('Hidden Singles');


hist1 = subplot(4,2,1);
histogram(veryEasy(:,3)./(81-veryEasy(:,12))*100,20);
title(hist1,'Very easy');
xlabel(hist1,'% of solving steps using Hidden Singles');
ylabel(hist1,'Nr of Sudokus');

hist2 = subplot(4,2,2);
histogram(easy(:,3)./(81-easy(:,12))*100,20);
title(hist2,'Easy');
xlabel(hist2,'% of solving steps using Hidden Singles');
ylabel(hist2,'Nr of Sudokus');

hist3 = subplot(4,2,3);
histogram(medium(:,3)./(81-medium(:,12))*100,20);
title(hist3,'Medium');
xlabel(hist3,'% of solving steps using Hidden Singles');
ylabel(hist3,'Nr of Sudokus');

hist4 = subplot(4,2,4);
histogram(hard(:,3)./(81-hard(:,12))*100,20);
title(hist4,'Hard');
xlabel(hist4,'% of solving steps using Hidden Singles');
ylabel(hist4,'Nr of Sudokus');

hist5 = subplot(4,2,5);
histogram(veryHard(:,3)./(81-veryHard(:,12))*100,20);
title(hist5,'Very Hard');
xlabel(hist5,'% of solving steps using Hidden Singles');
ylabel(hist5,'Nr of Sudokus');

hist6 = subplot(4,2,6);
histogram(evil(:,3)./(81-evil(:,12))*100,20);
title(hist6,'Very Hard Expert');
xlabel(hist6,'% of solving steps using Hidden Singles');
ylabel(hist6,'Nr of Sudokus');

hist7 = subplot(4,2,7);
histogram(exotic(:,3)./(81-exotic(:,12))*100,20);
title(hist7,'Evil&Exotic');
xlabel(hist7,'% of solving steps using Hidden Singles');
ylabel(hist7,'Nr of Sudokus');
