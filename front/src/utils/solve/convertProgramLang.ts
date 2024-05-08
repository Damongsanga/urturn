export const convertLangToUpper = (lang: string) => {
    switch (lang) {
        case 'c++':
            return 'CPP';
        case 'java':
            return 'JAVA';
        case 'python':
            return 'PYTHON';
        case 'javascript':
            return 'JAVASCRIPT';
    }
}