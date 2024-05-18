export const convertLangToUpper = (lang: string) : string | Error => {
    switch (lang) {
        case 'cpp':
            return 'CPP';
        case 'java':
            return 'JAVA';
        case 'python':
            return 'PYTHON';
        case 'javascript':
            return 'JAVASCRIPT';
    }
    return '';
}

export const convertUppserToLang = (lang: string) => {
    switch (lang) {
        case 'CPP':
            return 'cpp';
        case 'JAVA':
            return 'java';
        case 'PYTHON':
            return 'python';
        case 'JAVASCRIPT':
            return 'javascript';
    }
    return '';
}

export const convertLangToPrint = (lang: string) => {
    switch (lang) {
        case 'cpp':
            return 'C++';
        case 'java':
            return 'Java';
        case 'python':
            return 'Python';
        case 'javascript':
            return 'JavaScript';
    }
    return '';
}