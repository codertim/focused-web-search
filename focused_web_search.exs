

defmodule FocusedWebSearch do

  def search_links(web_links, args) do
    IO.puts "Searching urls ..."
    [search_term | _rest] = args
    :inets.start
    # :httpc.request(:get, {'http://www.erlang.org', []}, [], [])
    Enum.map(web_links, fn link -> 
      IO.puts "Calling link: #{link}"
      {:ok,{_status,_headers,content}} = :httpc.request('#{link}')
      # IO.puts "Content: #{content}   search_term = #{search_term}"
      if Regex.match?(~r/#{search_term}/, "#{content}") do
        IO.puts "!!!!! FOUND !!!!!"
      end
    end)
  end


  def process_file_contents(contents) do
    # IO.puts("Processing file contents #{contents}\n\n")
    lines = String.split(contents, "\n")
    # Enum.map(lines, fn l -> 
    #                      IO.puts "Line: #{l}"
    #                 end
    # )

    # ignore blank lines
    non_empty_lines = lines |> Stream.filter(fn l -> String.length(String.strip(l)) > 0 end) |> Enum.to_list

    search_links(non_empty_lines, System.argv())

    # IO.puts "Non-empty lines: #{List.to_string non_empty_lines}"
    # IO.puts "Non-empty lines:  " <> List.to_string(non_empty_lines)
    # Enum.map(non_empty_lines, fn l2 -> IO.puts "Non-empty line: #{l2}" end)
  end 
end


IO.puts "Starting ..."

filename = "links.txt"
IO.puts "Opening file: #{filename}"
case File.read(filename) do
  {:ok, body} -> FocusedWebSearch.process_file_contents(body)
  {:error, reason} -> IO.puts "Error: #{reason}"
end

# IO.puts "Command line arguments: " <> args

IO.puts "Done."



