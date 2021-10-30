package com.example.vocabulary.Bean;

import java.util.List;

public class TranslateBean {
    private String wordId;
    private String wordName;
    private List<SymbolsBean> symbols;

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public List<SymbolsBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolsBean> symbols) {
        this.symbols = symbols;
    }

    public static class SymbolsBean {
        private String symbolId;
        private String wordId;
        private String wordSymbol;
        private String symbolMp3;
        private List<PartsBean> parts;
        private String phAmMp3;
        private String phEnMp3;
        private String phTtsMp3;
        private String phOther;

        public String getSymbolId() {
            return symbolId;
        }

        public void setSymbolId(String symbolId) {
            this.symbolId = symbolId;
        }

        public String getWordId() {
            return wordId;
        }

        public void setWordId(String wordId) {
            this.wordId = wordId;
        }

        public String getWordSymbol() {
            return wordSymbol;
        }

        public void setWordSymbol(String wordSymbol) {
            this.wordSymbol = wordSymbol;
        }

        public String getSymbolMp3() {
            return symbolMp3;
        }

        public void setSymbolMp3(String symbolMp3) {
            this.symbolMp3 = symbolMp3;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public String getPhAmMp3() {
            return phAmMp3;
        }

        public void setPhAmMp3(String phAmMp3) {
            this.phAmMp3 = phAmMp3;
        }

        public String getPhEnMp3() {
            return phEnMp3;
        }

        public void setPhEnMp3(String phEnMp3) {
            this.phEnMp3 = phEnMp3;
        }

        public String getPhTtsMp3() {
            return phTtsMp3;
        }

        public void setPhTtsMp3(String phTtsMp3) {
            this.phTtsMp3 = phTtsMp3;
        }

        public String getPhOther() {
            return phOther;
        }

        public void setPhOther(String phOther) {
            this.phOther = phOther;
        }

        public static class PartsBean {
            private String partName;
            private List<MeansBean> means;

            public String getPartName() {
                return partName;
            }

            public void setPartName(String partName) {
                this.partName = partName;
            }

            public List<MeansBean> getMeans() {
                return means;
            }

            public void setMeans(List<MeansBean> means) {
                this.means = means;
            }

            public static class MeansBean {
                private String meanId;
                private String partId;
                private String wordMean;
                private String hasMean;
                private Integer split;

                public String getMeanId() {
                    return meanId;
                }

                public void setMeanId(String meanId) {
                    this.meanId = meanId;
                }

                public String getPartId() {
                    return partId;
                }

                public void setPartId(String partId) {
                    this.partId = partId;
                }

                public String getWordMean() {
                    return wordMean;
                }

                public void setWordMean(String wordMean) {
                    this.wordMean = wordMean;
                }

                public String getHasMean() {
                    return hasMean;
                }

                public void setHasMean(String hasMean) {
                    this.hasMean = hasMean;
                }

                public Integer getSplit() {
                    return split;
                }

                public void setSplit(Integer split) {
                    this.split = split;
                }
            }
        }
    }
}
