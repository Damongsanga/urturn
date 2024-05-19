export async function loadMarkdownFromCDN(url: string): Promise<string> {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(`Error fetching Markdown file: ${response.statusText}`);
      }
      const markdown = await response.text();
      return markdown;
    } catch (_error) {
      //console.error(error);
      return '';
    }
  }