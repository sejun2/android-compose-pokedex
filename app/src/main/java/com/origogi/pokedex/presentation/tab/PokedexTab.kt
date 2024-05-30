package com.origogi.pokedex.presentation.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.origogi.pokedex.domain.model.PokemonCardInfo
import com.origogi.pokedex.domain.model.PokemonType
import com.origogi.pokedex.presentation.components.PokemonCard
import com.origogi.pokedex.presentation.router.LocalNavScreenController
import com.origogi.pokedex.presentation.router.NavRoutes
import com.origogi.pokedex.presentation.theme.PokedexTheme
import com.origogi.pokedex.presentation.viewmodel.PokedexTabViewModel

@Composable
fun PokedexTab(
    modifier: Modifier = Modifier,
    viewModel: PokedexTabViewModel = hiltViewModel()
) {


    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                lastVisibleItem?.let {
                    val listSize = viewModel.list.size
                    if (it.index == listSize - 1) {
                        viewModel.loadMore()
                    }
                }
            }
    }

    Body(modifier = modifier, pokemonCardInfoList = viewModel.list, listState = listState)

}

@Composable
private fun Body(
    modifier: Modifier = Modifier,
    pokemonCardInfoList: List<PokemonCardInfo>,
    listState: LazyListState = LazyListState()
) {

    val navController = LocalNavScreenController.current

    Box(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            state = listState,
        ) {
            items(pokemonCardInfoList, key = {
                it.pokedexId
            }) { pokemonCardInfo ->
//                var lapVisible by remember { mutableStateOf(false) }
//                val animatedLapAlpha by animateFloatAsState(
//                    targetValue = if (lapVisible) 1f else 0f,
//                    label = "Lap alpha",
//                    animationSpec = tween(
//                        durationMillis = 250,
//                        easing = LinearEasing,
//                    )
//                )

                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate(NavRoutes.PokemonDetail.route + "/${pokemonCardInfo.pokedexId}")
                        }
//                        .graphicsLayer {
//                            lapVisible = true
//                            alpha = animatedLapAlpha
//                        }
                ) {
                    PokemonCard(pokemonCardInfo)
                }
            }
            if (pokemonCardInfoList.isNotEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

        }
    }
}

@Preview(name = "PokedexTab", showBackground = true)
@Composable
private fun PreviewPokedexBody() {
    PokedexTheme {
        Body(
            pokemonCardInfoList = listOf(
                PokemonCardInfo(
                    pokedexId = 1,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    types = listOf(PokemonType.Bug, PokemonType.Dark),
                ),
                PokemonCardInfo(
                    pokedexId = 12,
                    name = "Bulbasaur",
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    types = listOf(PokemonType.Ice, PokemonType.Dark),
                ),

                )
        )

    }
}