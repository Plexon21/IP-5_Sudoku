% Wird benutzt um eine Matrix einmalig zu randomisieren
load('__kti');
tmp= shuffledInput_kti(randperm(size(shuffledInput_kti,1)),:);
shuffledInput_kti = tmp;
